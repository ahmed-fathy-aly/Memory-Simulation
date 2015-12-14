package gui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.StringTokenizer;

import javax.swing.JComponent;

import models.Hole;
import models.MemorySpace;
import models.Process;

public class MemoryLayoutDrawer extends JComponent
{
	/* constants */
	public static final Color[] allColors = new Color[]
	{ Color.BLUE, Color.GREEN, Color.MAGENTA, Color.ORANGE, Color.PINK, Color.RED, Color.YELLOW };

	/* fields */
	private BufferedImage bufferedImage;
	private LinkedList<Color> remainingColors;
	private HashMap<String, Color> processColor;

	/* constructors */
	public MemoryLayoutDrawer()
	{
		this.remainingColors = new LinkedList<>();
		for (Color color : allColors)
			this.remainingColors.add(color);
		Collections.shuffle(remainingColors);
		this.processColor = new HashMap<>();
	}

	@Override
	protected void paintComponent(Graphics g)
	{
		if (this.bufferedImage != null)
			g.drawImage(bufferedImage, 0, 0, null);
	}

	private Color getColor(MemorySpace item)
	{
		// hole
		if (item.getClass().equals(Hole.class))
			return Color.CYAN;

		// each process has a name
		models.Process process = (Process) item;
		String procesName = process.getName();

		// already has a color
		if (processColor.containsKey(procesName))
			return processColor.get(procesName);

		// check we have a color remaining
		if (remainingColors.size() == 0)
		{
			for (Color color : allColors)
				remainingColors.add(color);
			Collections.shuffle(remainingColors);
		}

		// give it the first remaining color
		Color c = remainingColors.removeFirst();
		processColor.put(procesName, c);
		return c;
	}

	public void rePaint(List<Process> processes, List<Hole> holes)
	{
		// parse processes and holes
		ArrayList<MemorySpace> items = new ArrayList<>();
		items.addAll(processes);
		items.addAll(holes);

		// compute scale
		double scale = getScale(items);

		// sort according to start to find where the undefined space is
		Collections.sort(items, new Comparator<MemorySpace>()
		{
			@Override
			public int compare(MemorySpace i1, MemorySpace i2)
			{
				return Integer.compare(i1.getStart(), i2.getStart());
			}
		});

		// draw items
		this.bufferedImage = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_ARGB);
		for (MemorySpace item : items)
			drawItem(item, scale);
		repaint();

	}


	private void drawItem(MemorySpace item, double scale)
	{
		// compute params
		int startX = (int) (getWidth() * 1.0 / 10);
		int startY = (int) (item.getStart() * scale + 0.05 * getHeight());
		int width = getWidth() * 8 / 10;
		int height = (int) (item.getSize() * scale);

		// draw rectangle
		Graphics2D g2 = (Graphics2D) bufferedImage.getGraphics();
		g2.setStroke(new BasicStroke(3));
		g2.setColor(getColor(item));
		g2.fill3DRect(startX, startY, width, height, true);

		// draw border
		g2.setStroke(new BasicStroke(3));
		g2.setColor(Color.BLACK);
		g2.draw3DRect(startX, startY, width, height, true);

		// draw name
		g2.setColor(Color.BLACK);
		FontMetrics fm = g2.getFontMetrics();
		if (item.getClass().equals(Process.class))
		{
			Process process = (Process) item;
			Rectangle2D r = fm.getStringBounds(process.getName(), g2);
			int x = (this.getWidth() - (int) r.getWidth()) / 2;
			int y = startY + height / 2 - (int) r.getHeight() / 2 + fm.getAscent();
			g2.drawString(process.getName(), x, y);
		}

		// draw start index
		g2.setColor(Color.BLACK);
		Rectangle2D r = fm.getStringBounds(item.getSize() + "", g2);
		g2.drawString(item.getStart() + "", 0, startY + fm.getAscent());

		// draw end index
		g2.setColor(Color.BLACK);
		g2.drawString(item.getEnd() + "", 0, startY + height);

	}

	private double getScale(ArrayList<MemorySpace> items)
	{
		// compute max end
		int maxEnd = 0;
		for (MemorySpace item : items)
			maxEnd = Math.max(maxEnd, item.getEnd());
		return maxEnd == 0 ? 1 : 0.9 * getHeight() / maxEnd;
	}

	class Item
	{
		int start;
		int end;
		int size;
		String name;

		public Item(int start, int end, int size, String name)
		{
			this.start = start;
			this.end = end;
			this.size = size;
			this.name = name;
		}

		public String toString()
		{
			return name + " " + start + "->" + end;
		}
	}

	public void reset()
	{
		this.bufferedImage = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_ARGB);
		repaint();
	}
}

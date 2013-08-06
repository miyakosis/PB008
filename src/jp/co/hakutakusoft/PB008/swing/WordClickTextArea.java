package jp.co.hakutakusoft.PB008.swing;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JViewport;
import javax.swing.SwingUtilities;

public class WordClickTextArea extends JScrollPane
{
	public static WordClickTextArea	createComponent(final int id, final WordClickReceiver r)
	{
		final JTextArea	text = new JTextArea();		
        text.setEditable(false);
        text.setTabSize(4);
        text.setBorder(BorderFactory.createEmptyBorder(0,2,0,0));
        text.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 14));

        text.addMouseListener(
        		new MouseAdapter()
        		{
        			public void	mouseClicked(MouseEvent e)
        			{
        				if (SwingUtilities.isLeftMouseButton(e) && e.getClickCount() >= 2)
        				{
            				r.clicked(true, text.getSelectedText(), id);        					        					
        				}
        				else if (SwingUtilities.isRightMouseButton(e))
        				{
            				r.clicked(false, text.getSelectedText(), id);        					        					
        				}        				
        			}
        		});

        
        final LineNumberView	lnv = new LineNumberView(text);        
        
        WordClickTextArea scroll = new WordClickTextArea(text, id);
        scroll.setRowHeaderView(lnv);
        scroll.getVerticalScrollBar().addAdjustmentListener(
        		new AdjustmentListener()
        		{
					@Override
					public void adjustmentValueChanged(AdjustmentEvent arg0)
					{
						lnv.repaint();
					}       			
        		});
        
        scroll.setMinimumSize(new Dimension(0, 100));
        
        return	scroll;
	}	
	
	private final JTextArea	_text;
	private final int	_id;
	public WordClickTextArea(JTextArea text, int id)
	{
		super(text);
		_text = text;
		_id = id;
	}
		
	public void setText(String s, int pos, int size)
	{
		_text.setText(s);
		_text.setCaretPosition(0);
		
		final int paramPos = pos;
		final int paramSize = size;
		// 初期スクロール位置設定。表示後じゃないと位置設定できない模様
		_text.addComponentListener(
				new ComponentAdapter()
				{
					public void componentShown(ComponentEvent e)
					{	
						JViewport	v = WordClickTextArea.this.getViewport();
						// 該当行だとなぜか1行ずれるので調整
						int	y = (int)((v.getViewSize().getHeight() * (paramPos - 1)) / paramSize);
						v.setViewPosition(new Point(0, y));
//						System.out.println("pos:" + paramPos);
//						System.out.println("componentShown:" + y);
					}
				});
		// ここでイベント発生させてスクロール位置設定
		_text.setVisible(false);
		_text.setVisible(true);

		/*
		JScrollBar	scroll = getVerticalScrollBar();
		
		int	val = (scroll.getMaximum() * pos) / size;
//		scroll.setValue(val);
		
		
		JViewport	v = getViewport();
		Point	p = v.getViewPosition();
		
		Dimension	ds = v.getSize();
		Dimension	dv = v.getViewSize();
		int	val2 = (int)((v.getViewSize().getHeight() * pos) / size);
		
		p.y = val2;
//		v.setViewPosition(p);

		v.scrollRectToVisible(new Rectangle(0, val2, 100, 100));
		
		//		setViewport(v);
//		repaint();
		
		System.out.println("v_c:" + v.getComponentCount());
		System.out.println("v_c:" + v.getComponent(0));
		System.out.println("v_c:" + v.getComponent(0).isValid() + " " + v.getComponent(0).isVisible());

		
		System.out.println("dsH:" + ds.getHeight());
		System.out.println("dvH:" + dv.getHeight());

		System.out.println("H:" + _text.getHeight());
		System.out.println("P.y:" + p.y);
		*/
	}

	public void setText(String s)
	{
		_text.setText(s);
		_text.setCaretPosition(0);
		JScrollBar	scroll = getVerticalScrollBar();
		scroll.setValue(0);
	}

	public Component getTextComponent()
	{
		return _text;
	}	
}

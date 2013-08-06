package jp.co.hakutakusoft.PB008.swing;

import java.awt.Component;
import java.awt.Container;
import java.awt.Point;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class RefFrame extends JFrame
	implements WordClickReceiver
{
	private List<Component>	_cmps = new ArrayList<Component>();
	
	private CodeFrame	_parentFrame;
	private Dictionary	_dict;
	
	public RefFrame(CodeFrame f, Dictionary dict)
	{
		super();
		
		_parentFrame = f;
		_dict = dict;
//		setSize(f.getWidth(), f.getHeight());
//		Point	p = f.getLocation(null);
//		setLocation(p.x + f.getWidth(), p.y);
//		
////		setLocationRelativeTo(f);
		
		setTitle(CodeFrame.TITLE + " - ref");
		
		getContentPane().setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
	}
	
	
//	public void	setNewText(String word, boolean isDeclare)
//	{
//		addNewText(word, isDeclare, 0);
//		repaint();
//	}
//	
//	public void	addNewText(String word, boolean isDeclare, int windowIdx)
//	{
//		int	pos = 0;
//		String	refText = null;
//		// check valid word
//        if (isDeclare)
//        {
//        	pos = _parentFrame._dict.getDeclarePos(word);
//        	if (pos < 0)
//        	{
//        		return;
//        	}
//        }
//        else
//        {
//        	refText = _parentFrame._dict.getRef(word);
//        	if (refText == null)
//        	{
//        		return;
//        	}
//        }
//        
//		// add ok
//        Container	c = getContentPane();
//        System.out.println("c_1:" + c.getComponentCount());
//        while (c.getComponentCount() > windowIdx)
//        {
//			c.remove(c.getComponentCount() - 1);        	
//        }
//        System.out.println("c_2:" + c.getComponentCount());
//        
//        		
//		JTextArea	text = new JTextArea();
//        text.setEditable(false);
//        
//        if (isDeclare)
//        {
//            text.setText(_parentFrame._textStr);
//    		text.setCaretPosition(pos);        	
//        }
//        else
//        {
//            text.setText(refText);        	
//        }
//        
//		final LineNumberView	lnv = new LineNumberView(text);        
//		JScrollPane scroll = new JScrollPane(text);
//        scroll.setRowHeaderView(lnv);
//        scroll.getVerticalScrollBar().addAdjustmentListener(
//        		new AdjustmentListener()
//        		{
//					@Override
//					public void adjustmentValueChanged(AdjustmentEvent arg0)
//					{
//						lnv.repaint();
//					}       			
//        		});
//        
//        text.setBorder(BorderFactory.createEmptyBorder(0,2,0,0));
//        getContentPane().add(scroll);
//        
//        getContentPane().validate();
//	}

	
	private boolean	_isFirstShow = true;

	@Override
	public void clicked(boolean isDeclare, String word, int id)
	{
//		System.out.println(word);
		
		int	pos = 0;
		String	refText = null;
		// check valid word
        if (isDeclare)
        {
        	pos = _dict.getDeclarePos(word);
        	if (pos < 0)
        	{
        		return;
        	}
        }
        else
        {
        	refText = _dict.getRef(word);
        	if (refText == null)
        	{
        		return;
        	}
        }
//        System.out.println("" + pos + " " + refText);

        if (_isFirstShow)
		{	// adjust location
			setSize(_parentFrame.getWidth(), _parentFrame.getHeight());
			Point	p = _parentFrame.getLocation(null);
			setLocation(p.x + _parentFrame.getWidth(), p.y);

			_isFirstShow = false;
		}
		setVisible(true);
			
        
		// add ok
        Container	c = getContentPane();
//        System.out.println("c_1:" + c.getComponentCount());
        while (c.getComponentCount() > id)
        {
			c.remove(c.getComponentCount() - 1);        	
        }
//        System.out.println("c_2:" + c.getComponentCount());
        
        WordClickTextArea	text = WordClickTextArea.createComponent(id + 1, this);        
        if (isDeclare)
        {
            text.setText(_dict._s, pos, _dict._line);
        }
        else
        {
            text.setText(refText);        	
        }
        c.add(text);
        c.validate();
        
//        System.out.println("" + 
//        		text.getVerticalScrollBar().getValue() + " " +
//        		text.getVerticalScrollBar().getVisibleAmount());
	}	
}

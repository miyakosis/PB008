package jp.co.hakutakusoft.PB008.swing;

import java.awt.Component;
import java.awt.Cursor;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetAdapter;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JTextArea;
import javax.swing.JTextPane;


public class CodeFrame extends JFrame
{
	public final static String	TITLE = "AntlrGrammerViewer";
	
	private final WordClickTextArea	_text;

	private Dictionary	_dict = new Dictionary();
	
	private RefFrame	_refFrame;
	
	public CodeFrame()
	{
		super();
		_refFrame = new RefFrame(this, _dict);
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(400, 400);
		setLocationRelativeTo(null);
		setTitle(TITLE);
				
		WordClickTextArea	text = WordClickTextArea.createComponent(0, _refFrame);
		getContentPane().add(text);
		_text = text;
		
		
//		_text = new JTextArea();
////        _text = new JTextPane()
////        {
////    		@Override
////    		public boolean getScrollableTracksViewportWidth() {
////    			Object parent = getParent();
////    			if (parent instanceof JViewport) {
////    				JViewport port = (JViewport) parent;
////    				int w = port.getWidth();	// 表示できる範囲(上限)
////
////    				TextUI ui = getUI();
////    				Dimension sz = ui.getPreferredSize(this); // 実際の文字列サイズ
////    				if (sz.width < w) {
////    					return true;
////    				}
////    			}
////    			return false;
////    		}    		
////        };
//        _text.setEditable(false);
//
//        final LineNumberView	lnv = new LineNumberView(_text);        
//        
//		JScrollPane scroll = new JScrollPane(_text);
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
//        _text.setBorder(BorderFactory.createEmptyBorder(0,2,0,0));
//        add(scroll);

        
 //        getContentPane().add(new JScrollPane(new LineNumberView(_area)), BorderLayout.CENTER);

//        _text.addMouseListener(
//        		new MouseAdapter()
//        		{
//        			public void	mouseClicked(MouseEvent e)
//        			{
//        				if (SwingUtilities.isLeftMouseButton(e) && e.getClickCount() >= 2)
//        				{
//            				clicked(true);        					        					
//        				}
//        				else if (SwingUtilities.isRightMouseButton(e))
//        				{
//            				clicked(false);        					        					
//        				}        				
//        				
////        				if (SwingUtilities.isLeftMouseButton(e))
////        				{
////            				clicked(e.getModifiers());        					
////        				}
////        				else if (SwingUtilities.isRightMouseButton(e))
////        				{
////        					_text.processMouseEventExt(new MouseEvent(
////        							e.getComponent(),
////        							e.getID(),
////        							e.getWhen(),
////        							e.getModifiers(),
////        							e.getX(),
////        							e.getY(),
////        							e.getClickCount(),
////        							e.isPopupTrigger(),
////        							MouseEvent.BUTTON1
////        							));
////        				}
//        			}
//        		});
        
		new DropTarget(text.getTextComponent(),
				new DropTargetAdapter()
				{
					@Override
					public void drop(DropTargetDropEvent e)
					{
						 setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
				         try {
				                Transferable transfer = e.getTransferable();
				                // ファイルリストの転送を受け付ける
				                if (transfer.isDataFlavorSupported(DataFlavor.javaFileListFlavor)) {
				                    // copyとmoveを受け付ける
				                    e.acceptDrop(DnDConstants.ACTION_COPY_OR_MOVE);
				                    // ドラッグ＆ドロップされたファイルのリストを取得
				                    java.util.List<File> fileList =
				                        (List<File>) transfer.getTransferData(DataFlavor.javaFileListFlavor);
				                    if (fileList.size() > 0)
				                    {
				                    	File	f = fileList.get(0);
				                    	setText(f);				                    	
				                    }
				                }
				            } catch (UnsupportedFlavorException e1) {
				                e1.printStackTrace();
				            } catch (IOException e2) {
				                e2.printStackTrace();
				            }						
						 setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
					}
				});	
	}
	
	public void	setText(File f)	
		throws IOException
	{
    	StringBuilder	sb = new StringBuilder();
    	BufferedReader	reader = new BufferedReader(new FileReader(f));
    	int	c;
    	while ((c = reader.read()) >= 0)
    	{
    		sb.append((char)c);
    	}
    	reader.close();

    	String	s = sb.toString();

    	setTitle(TITLE + " - " + f.getName());
		
		_text.setText(s);
		_dict.parse(s);
	}

	
//	public void	clicked(boolean isDeclare)
//	{
////		int	pos = _text.getCaretPosition();
////		System.out.println("" + pos + " " + _textStr.charAt(pos) + " " + x);
////		System.out.println("" + isDeclare + " " + _text.getSelectedText());
//	
//		String	word = _text.getSelectedText();
//		System.out.println(word);
//		
//		if (_refFrame == null)
//		{
//			_refFrame = new RefFrame(this);
//		}
//		_refFrame.setVisible(true);
//		
//		_refFrame.setNewText(word, isDeclare);
//	}
//	
//	public static class	JTextPane2 extends JTextPane
//	{
//		public void	processMouseEventExt(MouseEvent e)
//		{
//			processMouseEvent(e);
//		}
//	}
}

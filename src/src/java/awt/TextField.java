/*
 * @(#)TextField.java	1.46 98/08/13
 *
 * Copyright 1995-1998 by Sun Microsystems, Inc.,
 * 901 San Antonio Road, Palo Alto, California, 94303, U.S.A.
 * All rights reserved.
 * 
 * This software is the confidential and proprietary information
 * of Sun Microsystems, Inc. ("Confidential Information").  You
 * shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement
 * you entered into with Sun.
 */
package java.awt;

import java.awt.peer.TextFieldPeer;
import java.awt.event.*;
import java.io.ObjectOutputStream;
import java.io.ObjectInputStream;
import java.io.IOException;


/**
 * A <code>TextField</code> object is a text component 
 * that allows for the editing of a single line of text.
 * <p>
 * For example, the following image depicts a frame with four
 * text fields of varying widths. Two of these text fields
 * display the predefined text <code>"Hello"</code>.
 * <p>
 * <img src="images-awt/TextField-1.gif"
 * ALIGN=center HSPACE=10 VSPACE=7>
 * <p>
 * Here is the code that produces these four text fields:
 * <p>
 * <hr><blockquote><pre>
 * TextField tf1, tf2, tf3, tf4; 
 * // a blank text field
 * tf1 = new TextField();
 * // blank field of 20 columns
 * tf2 = new TextField("", 20);
 * // predefined text displayed
 * tf3 = new TextField("Hello!");
 * // predefined text in 30 columns
 * tf4 = new TextField("Hello", 30);
 * </pre></blockquote><hr>
 * <p>
 * Every time the user types a key in the text field, AWT 
 * sends two action events to the text field. The first 
 * one represents the key press and the second one, 
 * the key release. Each action event embodies the state 
 * of the system at the time that some action occurred.
 * The properties of an action event indicate which 
 * key was pressed, what modifier keys were also pressed,
 * and the time at which the event occurred. 
 * <p>
 * Since the event is an instance of <code>ActionEvent</code>, 
 * the <code>TextField</code> class's <code>processEvent</code> 
 * method examines the event and passes it along to 
 * <code>processActionEvent</code>. The latter method redirects the
 * event to any <code>ActionListener</code> objects that have
 * registered an interest in action events generated by this
 * text field. 
 *
 * @version	1.46, 08/13/98
 * @author 	Sami Shaio
 * @see         java.awt.event.ActionEvent
 * @see         java.awt.TextField#processEvent
 * @see         java.awt.TextField#processActionEvent
 * @since       JDK1.0
 */
public class TextField extends TextComponent {

    /**
     * The number of columns in the TextField.
     */
    int columns;

    /**
     * The echo character.
     */
    char echoChar;

    transient ActionListener actionListener;

    private static final String base = "textfield";
    private static int nameCounter = 0;

    /*
     * JDK 1.1 serialVersionUID 
     */
    private static final long serialVersionUID = -2966288784432217853L;

    /**
     * Constructs a new text field.
     * @since      JDK1.0
     */
    public TextField() {
	this("", 0);
    }

    /**
     * Constructs a new text field initialized with the specified text.
     * @param      text       the text to be displayed.
     * @since      JDK1.0
     */
    public TextField(String text) {
	this(text, text.length());
    }

    /**
     * Constructs a new empty TextField with the specified number of columns.
     * @param columns the number of columns
     */ 
    public TextField(int columns) {
	this("", columns);
    }

    /**
     * Constructs a new text field initialized with the specified text
     * to be displayed, and wide enough to hold the specified 
     * number of characters.
     * @param      text       the text to be displayed.
     * @param      columns    the number of characters.
     * @since      JDK1.0
     */
    public TextField(String text, int columns) {
	super(text);
	this.columns = columns;
    }

    /**
     * Construct a name for this component.  Called by getName() when the
     * name is null.
     */
    String constructComponentName() {
        return base + nameCounter++;
    }

    /**
     * Creates the TextField's peer.  The peer allows us to modify the
     * appearance of the TextField without changing its functionality.
     */
    public void addNotify() {
      synchronized (getTreeLock()) {
	if (peer == null)
		peer = getToolkit().createTextField(this);
	super.addNotify();
      }
    }

    /**
     * Gets the character that is to be used for echoing.
     * <p>
     * An echo character is useful for text fields where 
     * user input should not be echoed to the screen, as in 
     * the case of a text field for entering a password.
     * @return      the echo character for this text field.
     * @see         java.awt.TextField#echoCharIsSet
     * @see         java.awt.TextField#setEchoChar
     * @since       JDK1.0
     */
    public char getEchoChar() {
	return echoChar;
    }

    /**
     * Sets the echo character for this text field. 
     * <p>
     * An echo character is useful for text fields where 
     * user input should not be echoed to the screen, as in 
     * the case of a text field for entering a password.
     * @param       c   the echo character for this text field.
     * @see         java.awt.TextField#echoCharIsSet
     * @see         java.awt.TextField#getEchoChar
     * @since       JDK1.1
     */
    public void setEchoChar(char c) {
	setEchoCharacter(c);
    }

    /**
     * @deprecated As of JDK version 1.1,
     * replaced by <code>setEchoChar(char)</code>.
     */
    public synchronized void setEchoCharacter(char c) {
	echoChar = c;
	TextFieldPeer peer = (TextFieldPeer)this.peer;
	if (peer != null) {
	    peer.setEchoCharacter(c);
	}
    }

    /**
     * Indicates whether or not this text field has a 
     * character set for echoing.
     * <p>
     * An echo character is useful for text fields where 
     * user input should not be echoed to the screen, as in 
     * the case of a text field for entering a password.
     * @return     <code>true</code> if this text field has 
     *                 a character set for echoing; 
     *                 <code>false</code> otherwise.
     * @see        java.awt.TextField#setEchoChar
     * @see        java.awt.TextField#getEchoChar
     * @since      JDK1.0
     */
    public boolean echoCharIsSet() {
	return echoChar != 0;
    }

    /**
     * Gets the number of columns in this text field. 
     * @return     the number of columns.
     * @see        java.awt.TextField#setColumns
     * @since      JDK1.1ld.
     */
    public int getColumns() {
	return columns;
    }

    /**
     * Sets the number of columns in this text field.
     * @param      columns   the number of columns.
     * @see        java.awt.TextField#getColumns
     * @exception  IllegalArgumentException   if the value
     *                 supplied for <code>columns</code> 
     *                 is less than zero.
     * @since      JDK1.1
     */
    public synchronized void setColumns(int columns) {
	int oldVal = this.columns;
	if (columns < 0) {
	    throw new IllegalArgumentException("columns less than zero.");
	}
	if (columns != oldVal) {
	    this.columns = columns;
	    invalidate();
	}
    }

    /**
     * Gets the preferred size of this text field 
     * with the specified number of columns.
     * @param     columns the number of columns 
     *                 in this text field. 
     * @return    the preferred dimensions for 
     *                 displaying this text field.
     * @since     JDK1.1
     */
    public Dimension getPreferredSize(int columns) {
        synchronized (getTreeLock()) {
    	    return preferredSize(columns);
        }
    }

    /**
     * @deprecated As of JDK version 1.1,
     * replaced by <code>getPreferredSize(int)</code>.
     */
    public Dimension preferredSize(int columns) {
        synchronized (getTreeLock()) {
	    TextFieldPeer peer = (TextFieldPeer)this.peer;
	    return (peer != null) ?
		       peer.preferredSize(columns) :
		       super.preferredSize();
        }
    }

    /**
     * Gets the preferred size of this text field. 
     * @return     the preferred dimensions for 
     *                         displaying this text field.
     * @since      JDK1.1
     */
    public Dimension getPreferredSize() {
    	return preferredSize();
    }

    /**
     * @deprecated As of JDK version 1.1,
     * replaced by <code>getPreferredSize()</code>.
     */
    public Dimension preferredSize() {
        synchronized (getTreeLock()) {
	    return (columns > 0) ?
		       preferredSize(columns) :
		       super.preferredSize();
        }
    }

    /**
     * Gets the minumum dimensions for a text field with 
     * the specified number of columns.
     * @param    columns   the number of columns in 
     *                          this text field.
     * @since    JDK1.1
     */
    public Dimension getMinimumSize(int columns) {
    	return minimumSize(columns);
    }

    /**
     * @deprecated As of JDK version 1.1,
     * replaced by <code>getMinimumSize(int)</code>.
     */
    public Dimension minimumSize(int columns) {
        synchronized (getTreeLock()) {
	    TextFieldPeer peer = (TextFieldPeer)this.peer;
	    return (peer != null) ?
		       peer.minimumSize(columns) :
		       super.minimumSize();
        }
    }

    /**
     * Gets the minumum dimensions for this text field.
     * @return     the minimum dimensions for 
     *                  displaying this text field.
     * @since      JDK1.1
     */
    public Dimension getMinimumSize() {
    	return minimumSize();
    }

    /**
     * @deprecated As of JDK version 1.1,
     * replaced by <code>getMinimumSize()</code>.
     */
    public Dimension minimumSize() {
        synchronized (getTreeLock()) {
	    return (columns > 0) ?
		       minimumSize(columns) :
		       super.minimumSize();
        }
    }

    /**
     * Adds the specified action listener to recieve 
     * action events from this text field.
     * @param      l the action listener.
     * @see        java.awt.event#ActionListener
     * @see        java.awt.TextField#removeActionListener
     * @since      JDK1.1
     */ 
    public synchronized void addActionListener(ActionListener l) {
	actionListener = AWTEventMulticaster.add(actionListener, l);
        newEventsOnly = true;	
    }

    /**
     * Removes the specified action listener so that it no longer
     * receives action events from this text field.
     * @param      l the action listener.
     * @see        java.awt.event#ActionListener
     * @see        java.awt.TextField#addActionListener
     * @since      JDK1.1 
     */ 
    public synchronized void removeActionListener(ActionListener l) {
	actionListener = AWTEventMulticaster.remove(actionListener, l);
    }

    // REMIND: remove when filtering is done at lower level
    boolean eventEnabled(AWTEvent e) {
        if (e.id == ActionEvent.ACTION_PERFORMED) {
            if ((eventMask & AWTEvent.ACTION_EVENT_MASK) != 0 ||
                actionListener != null) {
                return true;
            } 
            return false;
        }
        return super.eventEnabled(e);
    }          

    /**
     * Processes events on this text field. If the event 
     * is an instance of <code>ActionEvent</code>,
     * it invokes the <code>processActionEvent</code> 
     * method. Otherwise, it invokes <code>processEvent</code> 
     * on the superclass.
     * @param      e the event.
     * @see        java.awt.event.ActionEvent
     * @see        java.awt.TextField#processActionEvent
     * @since      JDK1.1
     */
    protected void processEvent(AWTEvent e) {
        if (e instanceof ActionEvent) {
            processActionEvent((ActionEvent)e);     
            return;
        }
	super.processEvent(e);
    }

    /** 
     * Processes action events occurring on this text field by
     * dispatching them to any registered 
     * <code>ActionListener</code> objects. 
     * <p>
     * This method is not called unless action events are 
     * enabled for this component. Action events are enabled 
     * when one of the following occurs:
     * <p><ul>
     * <li>An <code>ActionListener</code> object is registered 
     * via <code>addActionListener</code>.
     * <li>Action events are enabled via <code>enableEvents</code>.
     * </ul>
     * @param       e the action event.
     * @see         java.awt.event.ActionListener
     * @see         java.awt.TextField#addActionListener
     * @see         java.awt.Component#enableEvents
     * @since       JDK1.1
     */  
    protected void processActionEvent(ActionEvent e) {
        if (actionListener != null) {
            actionListener.actionPerformed(e);
        }
    }

    /**
     * Returns the parameter string representing the state of this 
     * text field. This string is useful for debugging. 
     * @return      the parameter string of this text field. 
     * @since       JDK1.0
     */
    protected String paramString() {
	String str = super.paramString();
	if (echoChar != 0) {
	    str += ",echo=" + echoChar;
	}
	return str;
    }


    /* Serialization support. 
     */

    private int textFieldSerializedDataVersion = 1;


    private void writeObject(ObjectOutputStream s)
      throws IOException 
    {
      s.defaultWriteObject();

      AWTEventMulticaster.save(s, actionListenerK, actionListener);
      s.writeObject(null);
    }


    private void readObject(ObjectInputStream s)
      throws ClassNotFoundException, IOException 
    {
      s.defaultReadObject();

      Object keyOrNull;
      while(null != (keyOrNull = s.readObject())) {
	String key = ((String)keyOrNull).intern();

	if (actionListenerK == key) 
	  addActionListener((ActionListener)(s.readObject()));

	else // skip value for unrecognized key
	  s.readObject();
      }
    }

}

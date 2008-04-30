/*
 This project is made available under the terms of the BSD license, more
 information can be found at
 http://www.opensource.org/licenses/bsd-license.html
 
 Copyright (c) 2007, Alexander Gitter
 
 All rights reserved.
*/

package th.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.plaf.basic.BasicArrowButton;

@SuppressWarnings( "serial" )
public class SelectPanel extends JPanel {

    private JLabel beprobungLabel = null;
    private MyBasicArrowButton previousButton = null;
    private MyBasicArrowButton nextButton = null;
    private NewButton newButton = null;
    private DeleteButton deleteButton = null;

    private Vector< SwitchListener > switchListeners;

    private int selectionCount;
    private int currentSelection;

    /**
     * This method initializes
     * 
     */
    public SelectPanel() {
        super();
        initialize();
    }

    /**
     * This method initializes this
     * 
     */
    private void initialize() {
        switchListeners = new Vector< SwitchListener >();
        FlowLayout flowLayout = new FlowLayout();
        flowLayout.setAlignment( FlowLayout.LEFT );
        beprobungLabel = new JLabel( "Seite: " );
        beprobungLabel.setPreferredSize( new Dimension( 150, beprobungLabel
                .getPreferredSize().height ) );
        this.setLayout( flowLayout );
        this.add( beprobungLabel, null );
        this.add( getPreviousButton(), null );
        this.add( getNextButton(), null );
        // this.add( getNewButton(), null );
        //this.add( getDeleteButton(), null );
    }

    /**
     * This method initializes previousButton
     * 
     * @return javax.swing.plaf.basic.BasicArrowButton
     */
    private BasicArrowButton getPreviousButton() {
        if( previousButton == null ) {
            previousButton = new MyBasicArrowButton( BasicArrowButton.WEST );

            previousButton.addActionListener( new ActionListener() {
                public void actionPerformed( ActionEvent e ) {
                    SelectPanel.this.fireUserSwitch( SwitchListener.PREVIOUS );
                }
            } );
        }
        return previousButton;
    }

    /**
     * This method initializes nextButton
     * 
     * @return javax.swing.plaf.basic.BasicArrowButton
     */
    private BasicArrowButton getNextButton() {
        if( nextButton == null ) {
            nextButton = new MyBasicArrowButton( BasicArrowButton.EAST );

            nextButton.addActionListener( new ActionListener() {
                public void actionPerformed( ActionEvent e ) {
                    SelectPanel.this.fireUserSwitch( SwitchListener.NEXT );
                }
            } );
        }
        return nextButton;
    }

    /**
     * This method initializes newButton
     * 
     * @return javax.swing.JButton
     */
    private NewButton getNewButton() {
        if( newButton == null ) {
            newButton = new NewButton();

            newButton.addActionListener( new ActionListener() {
                public void actionPerformed( ActionEvent e ) {
                    SelectPanel.this.fireUserSwitch( SwitchListener.NEW );
                }
            } );
        }
        return newButton;
    }

    private DeleteButton getDeleteButton() {
        if( deleteButton == null ) {
            deleteButton = new DeleteButton();
            deleteButton.setToolTipText( "Datensatz loeschen" );

            deleteButton.addActionListener( new ActionListener() {
                public void actionPerformed( ActionEvent e ) {
                    SelectPanel.this.fireUserSwitch( SwitchListener.DELETE );
                }
            } );
        }
        return deleteButton;
    }

    public void setSelectionCount( int value ) {
        selectionCount = value;
        updateUserInterface();
    }

    public void setCurrentSelection( int value ) {
        currentSelection = value;
        updateUserInterface();
    }

    private void updateUserInterface() {
        if( currentSelection == 0 || selectionCount == 0 ) {
            this.beprobungLabel.setText( "" );
        } else {
            this.beprobungLabel.setText( "Seite: " + currentSelection
                    + " / " + selectionCount );

            this.previousButton.setEnabled( true );
            this.nextButton.setEnabled( true );
            //this.deleteButton.setEnabled( true );

            //if( selectionCount <= 1 )
            //    this.deleteButton.setEnabled( false );

            if( currentSelection == 1 ) {
                this.previousButton.setEnabled( false );
            }

            if( currentSelection == selectionCount ) {
                this.nextButton.setEnabled( false );
            }
        }
    }

    public void addSwitchListener( SwitchListener sl ) {
        switchListeners.add( sl );
    }

    public void removeSwitchListener( SwitchListener sl ) {
        switchListeners.remove( sl );
    }

    protected void fireUserSwitch( int action ) {
        for( SwitchListener sl: switchListeners ) {
            sl.userSwitch( action, this.currentSelection );
        }
    }
}

class MyBasicArrowButton extends BasicArrowButton {
    public MyBasicArrowButton( int direction ) {
        super( direction );

        /*
         * this.addMouseListener( new MouseAdapter() { public void
         * mouseClicked(MouseEvent e) { //Since the user clicked on us, let's
         * get focus! requestFocusInWindow(); } });
         */
    }

    /*
     * public boolean isFocusTraversable() { return true; }
     */
}

class DeleteButton extends MyBasicArrowButton {
    private static final long serialVersionUID = 1L;
    private Color shadow;
    private Color darkShadow;

    public DeleteButton() {
        super( BasicArrowButton.NORTH );

        shadow = UIManager.getColor( "controlShadow" );
        darkShadow = UIManager.getColor( "controlDkShadow" );
    }

    public void paintTriangle( Graphics g, int x, int y, int size,
            int direction, boolean isEnabled ) {
        Color oldColor = g.getColor();

        size = Math.max( size, 2 );

        g.translate( x, y );

        if( isEnabled )
            g.setColor( darkShadow );
        else
            g.setColor( shadow );

        // g.drawLine( -size + 1, 1, size + 1, 1 );
        // g.drawLine( 1, -size + 1, 1, size + 1 );
        g.drawLine( -size + 2, -size + 2, size, size );
        g.drawLine( size, -size + 2, -size + 2, size );

        g.translate( -x, -y );
        g.setColor( oldColor );
    }
}

class NewButton extends MyBasicArrowButton {
    private static final long serialVersionUID = 1L;
    private Color shadow;
    private Color darkShadow;

    public NewButton() {
        super( BasicArrowButton.NORTH );

        shadow = UIManager.getColor( "controlShadow" );
        darkShadow = UIManager.getColor( "controlDkShadow" );
    }

    public void paintTriangle( Graphics g, int x, int y, int size,
            int direction, boolean isEnabled ) {
        Color oldColor = g.getColor();

        size = Math.max( size, 2 );

        g.translate( x, y );

        if( isEnabled )
            g.setColor( darkShadow );
        else
            g.setColor( shadow );

        g.drawLine( -size + 1, 1, size + 1, 1 );
        g.drawLine( 1, -size + 1, 1, size + 1 );
        g.drawLine( -size + 2, -size + 2, size, size );
        g.drawLine( size, -size + 2, -size + 2, size );

        g.translate( -x, -y );
        g.setColor( oldColor );
    }
}

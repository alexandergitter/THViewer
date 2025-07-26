/*
 This project is made available under the terms of the BSD license, more
 information can be found at
 http://www.opensource.org/licenses/bsd-license.html
 
 Copyright (c) 2007, Alexander Gitter
 
 All rights reserved.
*/

package thv.th.view;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.plaf.basic.BasicArrowButton;

@SuppressWarnings( "serial" )
public class SelectPanel
extends JPanel {

    private JLabel selectionLabel = null;
    private BasicArrowButton previousButton = null;
    private BasicArrowButton nextButton = null;

    private ArrayList< ISwitchListener > switchListeners;

    private int selectionCount;
    private int currentSelection;

    public SelectPanel() {
        super();
        initialize();
    }

    private void initialize() {
        switchListeners = new ArrayList< ISwitchListener >();
        FlowLayout flowLayout = new FlowLayout();
        flowLayout.setAlignment( FlowLayout.LEFT );
        selectionLabel = new JLabel( "Page: " );
        selectionLabel.setPreferredSize( new Dimension( 150, selectionLabel
                .getPreferredSize().height ) );
        this.setLayout( flowLayout );
        this.add( selectionLabel, null );
        this.add( getPreviousButton(), null );
        this.add( getNextButton(), null );
    }

    /**
     * This method initializes previousButton
     * 
     * @return javax.swing.plaf.basic.BasicArrowButton
     */
    private BasicArrowButton getPreviousButton() {
        if( previousButton == null ) {
            previousButton = new BasicArrowButton( BasicArrowButton.WEST );

            previousButton.addActionListener( new ActionListener() {
                public void actionPerformed( ActionEvent e ) {
                    SelectPanel.this.fireUserSwitch( ISwitchListener.PREVIOUS );
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
            nextButton = new BasicArrowButton( BasicArrowButton.EAST );

            nextButton.addActionListener( new ActionListener() {
                public void actionPerformed( ActionEvent e ) {
                    SelectPanel.this.fireUserSwitch( ISwitchListener.NEXT );
                }
            } );
        }
        return nextButton;
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
            this.selectionLabel.setText( "" );
        } else {
            this.selectionLabel.setText( "Page: " + currentSelection
                    + " / " + selectionCount );

            this.previousButton.setEnabled( true );
            this.nextButton.setEnabled( true );

            if( currentSelection == 1 ) {
                this.previousButton.setEnabled( false );
            }

            if( currentSelection == selectionCount ) {
                this.nextButton.setEnabled( false );
            }
        }
    }

    public void addSwitchListener( ISwitchListener sl ) {
        switchListeners.add( sl );
    }

    public void removeSwitchListener( ISwitchListener sl ) {
        switchListeners.remove( sl );
    }

    protected void fireUserSwitch( int action ) {
        for( ISwitchListener sl: switchListeners ) {
            sl.userSwitch( action, this.currentSelection );
        }
    }
}

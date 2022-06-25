/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.draganddrop;

import java.awt.datatransfer.DataFlavor;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDropEvent;
import java.io.File;
import java.util.List;
import javax.swing.JFrame;
import javax.swing.JTextArea;

/**
 *
 * @author Overthink
 */
public class Common extends JFrame{
    public Common(){
        super("test");
        
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(600,600);
        
        
        JTextArea myPanel = new JTextArea();
        myPanel.setDropTarget(new DropTarget() {
            public synchronized void drop(DropTargetDropEvent evt) {
                try {
                    evt.acceptDrop(DnDConstants.ACTION_COPY);
                    List<File> droppedFiles = (List)(evt.getTransferable().getTransferData(DataFlavor.javaFileListFlavor));
                    //System.out.println(droppedFiles);
                    for (File file : droppedFiles) {
                        System.out.println(file);
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });
        
        getContentPane().add(myPanel);
        setVisible(true);
        
        
    }
    
    
    public static void main(String args[]){
        new Common();
    }
}

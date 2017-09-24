package org.parison.cool;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;

public  class Fenetre extends JFrame {

    JFrame izy;
    final static Logger LOGGER = Logger.getLogger(Fenetre.class);
    static ApplicationContext applicationContext;

    public Fenetre (ApplicationContext applicationContext) {
        Fenetre.applicationContext = applicationContext;
        this.setTitle("Uniformizer App");
        this.setSize(300,175);
        this.getContentPane().setBackground(Color.WHITE);
        this.setResizable(false);
        this.setLocationRelativeTo(null);

        JMenuBar menubar = new JMenuBar();
        JMenu menu = new JMenu();
        menu.setText("About");
        menu.setMnemonic('A');
        menu.setRolloverEnabled(true);
        JMenuItem item = new JMenuItem();
        item.setText("About the application");
        menu.add(item);
        menubar.add(menu);

        this.setVisible(true);
        this.getContentPane().setLayout(null);
        this.getContentPane().add(menubar);
        menubar.setLocation(0,0);
        menubar.setSize(300,30);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        izy = this;


        item.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {

                JOptionPane optionPane = new JOptionPane();
                optionPane.showMessageDialog(izy,
                        "Created by Parison Rvlmd.\n","A propos de l'auteur",JOptionPane.INFORMATION_MESSAGE);
            }
        });

        JButton bou = new JButton("GO");
        bou.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {

                LOGGER.debug("Beginning application execution ");
                File theseExcel = new File(System.getProperty("user.dir")+"\\etc\\these.xlsx");
                try {
                    if ( theseExcel.exists()) {
                        InputStream fileIn = new FileInputStream(theseExcel);
                        LOGGER.debug(" Creating input stream from " + theseExcel.getPath());
                        CheckService checkService = new CheckService(fileIn);
                        checkService.processExcelFile();
                        LOGGER.debug(" Sending email with attachement");
                        MailService mailService = new MailService(Fenetre.applicationContext);
                        mailService.sendEmail();
                    }
                    else {
                        LOGGER.warn("Le fichier "+theseExcel.getPath()+" n'existe pas");
                        System.exit(1);
                    }

                } catch (IOException e) {

                }

            }

        });

        bou.setSize(70,35);
        this.getContentPane().add(bou);
        bou.setLocation(100,80);
    }
}
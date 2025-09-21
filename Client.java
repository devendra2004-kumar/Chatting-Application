import javax.swing.*;

import java.net.Socket;
import java.awt.*;
import java.awt.event.*;
import javax.swing.border.*;
import java.util.*;
import java.text.*;
import java.net.*;
import java.io.*;


public class Client  implements ActionListener {

    JTextField text;  //is used in client constructor and in a function also
    static JPanel a1; 
    static Box vertical = Box.createVerticalBox();  //create vertical box for showing sender msg ek ke niche ek.
    static DataOutputStream dout;
    static JFrame f = new JFrame();

    Client(){
        f.setLayout(null);     //for complete window
        //Green panel
        JPanel p1 = new JPanel();
        p1.setBackground(new Color(7,94,84));
        p1.setBounds(0,0,450,60);
        p1.setLayout(null);     //For green Panel only
        f.add(p1);

        // for back arrow
        ImageIcon i1 = new ImageIcon(ClassLoader.getSystemResource("resource/3.png"));
        Image i2 = i1.getImage().getScaledInstance(25, 25, Image.SCALE_DEFAULT);
        ImageIcon i3 = new ImageIcon(i2);
        JLabel back = new JLabel(i3);
        back.setBounds(5,20,25,25);
        p1.add(back);

         // close window on back arrow click     -->CLOSING EVENT   
        back.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent ae){
                f.setVisible(false);
                //System.exit(0);
            }
        });

        //for profile pictutre
        ImageIcon i4 = new ImageIcon(ClassLoader.getSystemResource("resource/2.png"));
        Image i5 = i4.getImage().getScaledInstance(40, 40, Image.SCALE_DEFAULT);
        ImageIcon i6 = new ImageIcon(i5);
        JLabel profile = new JLabel(i6);
        profile.setBounds(30,8,50,50);
        p1.add(profile);

        //for video icon
        ImageIcon i7 = new ImageIcon(ClassLoader.getSystemResource("resource/video.png"));
        Image i8 = i7.getImage().getScaledInstance(25, 25, Image.SCALE_DEFAULT);
        ImageIcon i9 = new ImageIcon(i8);
        JLabel video_icon = new JLabel(i9);
        video_icon.setBounds(300,20,25,25);
        p1.add(video_icon);

        //for phone icon
        ImageIcon i10 = new ImageIcon(ClassLoader.getSystemResource("resource/phone.png"));
        Image i11 = i10.getImage().getScaledInstance(25, 25, Image.SCALE_DEFAULT);
        ImageIcon i12 = new ImageIcon(i11);
        JLabel phone = new JLabel(i12);
        phone.setBounds(350,20,25,25);
        p1.add(phone);

        //for three dot  icon(morevert)
        ImageIcon i13 = new ImageIcon(ClassLoader.getSystemResource("resource/icon.png"));
        Image i14 = i13.getImage().getScaledInstance(15, 25, Image.SCALE_DEFAULT);
        ImageIcon i15 = new ImageIcon(i14);
        JLabel icon = new JLabel(i15);
        icon.setBounds(395,20,15,25);
        p1.add(icon);

        //for showing name using JLabel
        JLabel name = new JLabel("ABHISHEK");
        name.setBounds(90,5,100,45);
        name.setForeground(Color.WHITE);
        p1.add(name);

        //for status using JLabel
        JLabel status = new JLabel("Active");
        status.setBounds(90,30,60,25);
        status.setForeground(Color.WHITE);
        p1.add(status);

        // Messaging area
        a1 = new JPanel();  //declared globally
        a1.setBounds(5,70,425,535);
        f.add(a1);

        //Text field area (Typing wali jagah)
        text = new JTextField(); // -->this is declared globally
        text.setBounds(5,610,310,40);
        text.setFont(new Font("SAN_SERIF",Font.PLAIN,17));
        f.add(text);

        //Send Button
        JButton send = new JButton("Send");
        send.setBounds(320,610,110,38);
        send.setBackground(new Color(7,94,84));
        send.setFont(new Font("SAN_SERIF",Font.PLAIN,17));
        send.setForeground(Color.WHITE);
        f.add(send);
        send.addActionListener(this);    //action listener for send button-->its action is written in actionPerformed().

       

        //complete window
        f.setSize(450,700);
        f.setLocation(800,50);
        f.getContentPane().setBackground(Color.white);

        f.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent ae){
       try {
        String out = text.getText(); //extracting msg from text field
        a1.setLayout(new BorderLayout());//a1 panel ke uper border layout form showing msg in a formatted way(L,R,T,D C)

      // JLabel output = new JLabel(out); //out cannot added to right panel, created a JLabel   -->not needed(2)..............
       //not needed, instead this --> JPanel p2 = new JPanel();
       JPanel p2 =formatLabel(out);
      //  p2.add(output); //output('out') is added to panel p2 ...............
       
        JPanel right = new JPanel(new BorderLayout());
        right.add(p2, BorderLayout.LINE_END); // since it does not take String as input we cannot put 'out' directly, we use a JLabel to show our msg
        vertical.add(right); // 'right' panel is added to 'vertical' box
        vertical.add(Box.createVerticalStrut(15));

        a1.add(vertical, BorderLayout.PAGE_START); //vertical box is finally placed over 'a1' panel
        dout.writeUTF(out);
        text.setText("");

        f.repaint();
        f.invalidate();
        f.validate();
    }catch(Exception e){
        e.printStackTrace();
    }
    }

     //  formatting the msg
     public static JPanel formatLabel(String out){
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        JLabel output = new JLabel("<html><p style=\"width : 150px\">" + out + "</p></html>");
        output.setFont(new Font("Tahoma",Font.PLAIN,16));
        output.setBackground(new Color(86,199,122));
        output.setOpaque(true);
        output.setBorder(new EmptyBorder(15,15,15,50)); 

        panel.add(output);

        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        JLabel time = new JLabel(); 
        time.setText(sdf.format(cal.getTime()));
        panel.add(time);

        return panel;
     }
    
    public static void main(String[] args) {
        new Client();
        try{
            Socket s = new Socket("127.0.0.1",6001);
            DataInputStream din = new DataInputStream(s.getInputStream());
            dout = new DataOutputStream(s.getOutputStream());

            while(true){
                a1.setLayout(new BorderLayout());
                String msg = din.readUTF();
                JPanel panel = formatLabel(msg);

                JPanel left = new JPanel(new BorderLayout());
                left.add(panel, BorderLayout.LINE_START);
                vertical.add(left);                             // cannot call non static method, so make its reference static

                vertical.add(Box.createVerticalStrut(15));
                a1.add(vertical, BorderLayout.PAGE_START);
                f.validate();
            }

        }catch(Exception e){
            e.printStackTrace();
        }
    }
}



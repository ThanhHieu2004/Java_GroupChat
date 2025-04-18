package com.example.demo.TestChoTuan;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class ChatClientGUI extends JFrame {
    private JTextArea messageArea;
    private JTextField textField;
    private JButton exitButton;
    private Map<String, JTextArea> chatAreas = new HashMap<>();
    private JScrollPane messageScrollPane;
    private ChatClient client;

    public ChatClientGUI() {
        super("Ứng dụng chat");
        setSize(800, 500);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        // Các biến style
        Color exitButtonTextColor = new Color(255, 255, 255);
        Font exitButtonFont = new Font("Arial", Font.BOLD, 12);

        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
        leftPanel.setBackground(new Color(30, 30, 30));

        String[] sampleRooms = { "Công nghệ số", "Điện-Điện tử", "Cơ khí", "Điện tử viễn thông", "Hóa-môi trường" };
        for (String room : sampleRooms) {
            JLabel label = new JLabel(room);
            label.setForeground(Color.WHITE);
            label.setFont(new Font("Comic Sans MS", Font.PLAIN, 14));
            label.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
            label.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

            // Create khu vực chat cho mỗi phòng
            JTextArea roomArea = new JTextArea();
            roomArea.setEditable(false);
            roomArea.setBackground(new Color(0, 0, 0));
            roomArea.setForeground(Color.WHITE);
            roomArea.setFont(new Font("Comic Sans MS", Font.PLAIN, 14));
            chatAreas.put(room, roomArea);

            label.addMouseListener(new MouseAdapter() {//Add event cho label
                @Override
                public void mouseClicked(MouseEvent e) {
                    for (Component com : label.getParent().getComponents()) {
                        if (com instanceof JLabel){
                            com.setBackground(new Color(0, 0, 0));
                            ((JLabel) com).setForeground(Color.white);
                            ((JLabel) com).setOpaque(false);
                        }
                    }
                    label.setBackground(Color.CYAN);
                    label.setForeground(Color.WHITE);
                    label.setOpaque(true);
                    messageScrollPane.setViewportView(chatAreas.get(room));
                }

                @Override
                public void mouseEntered(MouseEvent e) {
                    label.setForeground(Color.CYAN);
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    label.setForeground(Color.WHITE);
                }
            });

            leftPanel.add(label);
        }

        JScrollPane leftScrollPane = new JScrollPane(leftPanel);
        leftScrollPane.setPreferredSize(new Dimension(140, 0));
        add(leftScrollPane, BorderLayout.WEST);

        // Áp dụng style cho khu vực chat
        messageArea = new JTextArea();
        messageArea.setEditable(false);
        messageArea.setBackground(new Color(0, 0, 0));
        messageArea.setForeground(Color.WHITE);
        messageArea.setFont(new Font("Comic Sans MS", Font.PLAIN, 14));
        messageScrollPane = new JScrollPane(messageArea);
        add(messageScrollPane, BorderLayout.CENTER);

        // Set chat room mặc định
        messageScrollPane.setViewportView(chatAreas.get("Công nghệ số"));

        textField = new JTextField();
        textField.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                client.sendMessage(textField.getText());
                textField.setText("");
            }
        });
        add(textField, BorderLayout.SOUTH);

        // Prompt bắt nhập username khi chạy app
        String name = JOptionPane.showInputDialog(this, "Nhập tên của bạn:", "Tên", JOptionPane.PLAIN_MESSAGE);
        this.setTitle("Ứng dụng chat - " + name);
        textField.setFont(new Font("Comic Sans MS", Font.PLAIN, 14));
        // Hàm lamda tạo format ngày đi kèm với tin nhắn sau khi gửi
        textField.addActionListener(e -> {
            String currentRoom = null;
        
            for (Map.Entry<String, JTextArea> entry : chatAreas.entrySet()) {//Duyệt qua các phòng
                if (messageScrollPane.getViewport().getView() == entry.getValue()) {//Kiểm tra nếu phòng hiện tại là entry
                    currentRoom = entry.getKey();
                    break;
                }
            }
        
            if (currentRoom != null) {//Gửi tin nhắn đến phòng
                String message = "[" + new SimpleDateFormat("HH:mm").format(new Date()) + "]" 
                                 + currentRoom + ": " + name + ": " + textField.getText();
                client.sendMessage(message);
            }
        
            textField.setText("");
        });
        

        // Thêm nút Thoát với mỗi Client, hiện thị tin nhắn "Đã thoát" sau khi ấn
        exitButton = new JButton("Thoát");
        exitButton.setBackground(new Color(0, 0, 0));
        exitButton.setFont(exitButtonFont);
        exitButton.setForeground(exitButtonTextColor);
        exitButton.addActionListener(e -> {
            String departureMessage = name + " đã thoát.";
            client.sendMessage(departureMessage);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException ie) {
                Thread.currentThread().interrupt();
            }
            System.exit(0);
        });

        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setBackground(new Color(0, 0, 0));
        bottomPanel.add(textField, BorderLayout.CENTER);
        bottomPanel.add(exitButton, BorderLayout.EAST);
        add(bottomPanel, BorderLayout.SOUTH);

        // Khởi tạo và bắt đầu Client
        try {
//            this.client = new ChatClient("localhost", 5000, this::onMessageReceived);
        	this.client = new ChatClient("192.168.2.29", 5000, this::onMessageReceived);
            client.startClient();
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Lỗi kết nối đến Server", "Lỗi kết nối",
                    JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }
    }

    private void onMessageReceived(String message) {
        SwingUtilities.invokeLater(() -> {
            //Lấy tên room
            int roomStart = message.indexOf("]") + 1;
            int roomEnd = message.indexOf(":", roomStart);

            if (roomStart > 0 && roomEnd > roomStart) {
                String room = message.substring(roomStart, roomEnd).trim();
                JTextArea roomArea = chatAreas.getOrDefault(room, messageArea);
                roomArea.append(message + "\n");
            } else {
                messageArea.append(message + "\n");//Nếu tin nhắn k theo chuẩn (ví dụ: [15:03]tên room - tên user: tin nhắn) thì đưa về khu vực nhắn tin mặc định (messageArea)
             }
        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new ChatClientGUI().setVisible(true);
        });
    }
}
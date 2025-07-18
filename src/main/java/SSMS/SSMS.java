package SSMS;

import java.util.ArrayList;
import java.util.Scanner;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.FileReader;

public class SSMS {
    public static void main(String[] args) {
        ArrayList<Student> students = new ArrayList<Student>();
        Scanner scanner = new Scanner(System.in);
        try (BufferedReader reader = new BufferedReader(new FileReader("D:\\mine\\个人\\实习\\学生成绩管理系统.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }


        try {
            boolean running = true;
            while (running) {
                System.out.println("\n学生成绩管理系统");
                System.out.println("1. 添加学生");
                System.out.println("2. 查看所有学生");
                System.out.println("3. 搜索学生");
                System.out.println("4. 退出");
                System.out.println("请选择操作(1-4):");

                String choice = scanner.nextLine();

                switch (choice) {
                    case "1":
                        addStudent(students, scanner);
                        break;
                    case "2":
                        displayAllStudents(students);
                        break;
                    case "3":
                        searchStudents(students, scanner);
                        break;
                    case "4":
                        running = false;
                        break;
                    default:
                        System.out.println("无效选择，请重试");
                }

            }
        } finally {
            exportToFile(students);
            scanner.close();
        }
    }

    // 添加学生
    private static void addStudent(ArrayList<Student> students, Scanner scanner) {
        Student student = new Student();

        System.out.println("请输入学生姓名");
        student.setName(scanner.nextLine());

        System.out.println("请输入成绩");
        String inputScore = scanner.nextLine();

        while (!inputScore.matches("\\d+(\\.\\d+)?")) {
            System.out.println("输入无效，请输入一个数字");
            inputScore = scanner.nextLine();
        }

        student.setScore(Double.parseDouble(inputScore));
        students.add(student);
        System.out.println("学生添加成功！");
    }

    // 搜索学生
    private static void searchStudents(ArrayList<Student> students, Scanner scanner) {
        if (students.isEmpty()) {
            System.out.println("没有学生记录");
            return;
        }

        System.out.println("\n搜索选项:");
        System.out.println("1. 按姓名搜索");
        System.out.println("2. 按成绩范围搜索");
        System.out.println("请选择搜索方式(1-2):");

        String searchChoice = scanner.nextLine();

        switch (searchChoice) {
            case "1":
                searchByName(students, scanner);
                break;
            case "2":
                searchByScoreRange(students, scanner);
                break;
            default:
                System.out.println("无效选择");
        }
    }

    // 按姓名搜索
    private static void searchByName(ArrayList<Student> students, Scanner scanner) {
        System.out.println("请输入要搜索的学生姓名:");
        String nameQuery = scanner.nextLine();

        ArrayList<Student> results = new ArrayList<>();
        for (Student student : students) {
            if (student.getName().contains(nameQuery)) {
                results.add(student);
            }
        }

        displaySearchResults(results);
    }

    // 按成绩范围搜索
    private static void searchByScoreRange(ArrayList<Student> students, Scanner scanner) {
        System.out.println("请输入最低成绩:");
        double minScore = getValidScore(scanner);

        System.out.println("请输入最高成绩:");
        double maxScore = getValidScore(scanner);

        // 确保最低分不高于最高分
        if (minScore > maxScore) {
            double temp = minScore;
            minScore = maxScore;
            maxScore = temp;
        }

        ArrayList<Student> results = new ArrayList<>();
        for (Student student : students) {
            if (student.getScore() >= minScore && student.getScore() <= maxScore) {
                results.add(student);
            }
        }

        displaySearchResults(results);
    }

    // 获取有效的成绩输入
    private static double getValidScore(Scanner scanner) {
        String input = scanner.nextLine();
        while (!input.matches("\\d+(\\.\\d+)?")) {
            System.out.println("输入无效，请输入一个数字:");
            input = scanner.nextLine();
        }
        return Double.parseDouble(input);
    }

    // 显示搜索结果
    private static void displaySearchResults(ArrayList<Student> results) {
        if (results.isEmpty()) {
            System.out.println("未找到匹配的学生");
        } else {
            System.out.println("\n搜索结果 - 找到 " + results.size() + " 名学生:");
            for (int i = 0; i < results.size(); i++) {
                Student student = results.get(i);
                System.out.println((i + 1) + ". 姓名: " + student.getName() + ", 成绩: " + student.getScore());
            }
        }
    }

    // 显示所有学生
    private static void displayAllStudents(ArrayList<Student> students) {
        if (students.isEmpty()) {
            System.out.println("没有学生记录");
        } else {
            System.out.println("\n所有学生列表:");
            for (int i = 0; i < students.size(); i++) {
                Student student = students.get(i);
                System.out.println((i + 1) + ". 姓名: " + student.getName() + ", 成绩: " + student.getScore());
            }
        }
    }

    // 导出文件
    private static void exportToFile(ArrayList<Student> students) {
        // 计算平均分
        double totalScore = 0;
        for (Student student : students) {
            totalScore += student.getScore();
        }
        double averageScore = totalScore / students.size();

        try (BufferedWriter writer = new BufferedWriter(new FileWriter("D:\\mine\\个人\\实习\\学生成绩管理系统.txt"))) {
            writer.write("学生成绩管理系统\n");
            writer.write("总人数: " + students.size() + "\n");
            writer.write("平均分: " + averageScore + "\n");
            writer.write("学生列表:\n");

            // 按成绩从高到低排序
            students.sort((s1, s2) -> Double.compare(s2.getScore(), s1.getScore()));

            for (Student student : students) {
                writer.write("姓名: " + student.getName() + ", 成绩: " + student.getScore() + "\n");
            }
            System.out.println("成绩已成功导出到文件");
        } catch (IOException e) {
            System.out.println("导出失败: " + e.getMessage());
        }
    }
}
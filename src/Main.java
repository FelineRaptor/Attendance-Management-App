import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Objects;
import java.util.Scanner;

public class Main
{
    //TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the
    boolean isCreated = false;
    boolean attendanceTaken = false;
    int day;
    void main() {


        boolean exit = false;
        int subCount = 3;
        int choice;

        Scanner scanner = new Scanner(System.in);

        Subject[] subs = new Subject[subCount];

        for (int i = 0; i < subCount; i++) {
            subs[i] = new Subject();
        }


        LoadData(subs);

        LocalDate today = LocalDate.now();
        if (day != today.getDayOfMonth()) {
            attendanceTaken = false;
            day = today.getDayOfMonth();
        }

        if (!isCreated)
        {
            for (int i = 0; i < subCount; i++)
            {
                System.out.print("Enter " + (i+1) + " subject name: ");

                String subjectname = scanner.nextLine();
                subs[i] = CreateSubject(subjectname);
            }

            isCreated = true;
        }

        System.out.println("1. Enter Attendance");
        System.out.println("2. View Subject-wise Attendance");
        System.out.println("3. View Overall Attendance");
        System.out.println("4. Exit");

        while (!exit)
        {
            System.out.print("Choose an option: ");
            choice = scanner.nextInt();
            scanner.nextLine();
            switch (choice)
            {
                case 1:
                    if (!attendanceTaken) {
                        for (int i = 0; i < subCount; i++) {
                            System.out.print("Did " + subs[i].subName + " class happen today: ");
                            boolean isClass = scanner.nextBoolean();
                            if (isClass) {
                                System.out.print("Present?: ");
                                boolean isPresent = scanner.nextBoolean();
                                EnterAttendance(subs[i], true, isPresent);
                            }
                            else {
                                EnterAttendance(subs[i], false, false);
                            }
                        }
                        attendanceTaken = true;
                    }
                    else
                    {
                        System.out.println("Attendance already taken");
                    }
                    SaveData(subs);
                    break;
                case 2:
                    System.out.print("Enter subject: ");
                    String subjectname = scanner.nextLine();
                    ViewAttendance(FindSubject(subjectname, subs));
                    break;
                case 3:
                    ViewAllAttendance(subs);
                    break;
                case 4:
                    exit = true;
                    break;
                default:
                    System.out.println("Enter valid input");
                    break;
            }
        }
    }

    void SaveData(Subject[] sub)
    {
        try(FileWriter writer = new FileWriter("attendanceData.txt"))
        {
            writer.write(isCreated + "\n");
            writer.write(attendanceTaken + "\n");
            writer.write(day + "\n");


            for (Subject subject : sub) {
                writer.write(subject.subName + "\n");
                writer.write(subject.dayCount + "\n");
                writer.write(subject.presentCount + "\n");
                writer.write(subject.attendPercent + "\n");
            }

        }
        catch (IOException e) {
            System.out.println("Could not read file");
        }

    }

    void LoadData(Subject[] sub)
    {
        try(BufferedReader reader = new BufferedReader(new FileReader("attendanceData.txt")))
        {
            isCreated = Boolean.parseBoolean(reader.readLine());
            attendanceTaken = Boolean.parseBoolean(reader.readLine());
            day = Integer.parseInt(reader.readLine());
            for (Subject subject : sub) {
                subject.subName = String.valueOf(reader.readLine());
                subject.dayCount = Float.parseFloat(reader.readLine());
                subject.presentCount = Float.parseFloat(reader.readLine());
                subject.attendPercent = Float.parseFloat(reader.readLine());
            }

        }
        catch (IOException e) {
            System.out.println("No data found, initializing values");
        }

    }

    Subject CreateSubject(String subname)
    {
        Subject sub = new Subject();
        sub.subName = subname;
        return sub;
    }

    void EnterAttendance(Subject sub, boolean isClass, boolean isPresent)
    {
        sub.dayCount = isClass? sub.dayCount + 1  : sub.dayCount + 0;
        sub.presentCount = isPresent? sub.presentCount + 1 : sub.presentCount + 0;
        sub.attendPercent = (sub.presentCount/sub.dayCount) * 100;

        System.out.println(sub.subName);
        System.out.println(sub.attendPercent);
    }

    Subject FindSubject(String name, Subject[] subAr)
    {
        int count = 0;
        Subject sub = new Subject();
        for(Subject subject : subAr)
        {
            if (Objects.equals(subject.subName, name))
            {
                count++;
                sub = subject;
                break;
            }
        }

        if (count == 0) {
            System.out.println("Subject not found");
        }

        return sub;
    }

    void ViewAttendance(Subject sub)
    {
        System.out.println("Subject Name: " + sub.subName);
        System.out.println("Number of days of class so far: " + (int)sub.dayCount);
        System.out.println("Number of days present: " + (int)sub.presentCount);
        System.out.println("Number of days absent: " + (int)(sub.dayCount - sub.presentCount));
        System.out.println("Attendance Percentage: " + sub.attendPercent);
    }

    void ViewAllAttendance(Subject[] subArr)
    {
        float totalpresent = 0;
        float totalclass = 0;
        float totalPercent;
        System.out.print("\n");
        for (Subject subject : subArr) {
            System.out.println("Subject Name: " + subject.subName);
            System.out.println("Number of days of class so far: " + (int)subject.dayCount);
            System.out.println("Number of days present: " + (int)subject.presentCount);
            System.out.println("Number of days absent: " + (int)(subject.dayCount - subject.presentCount));
            System.out.printf("Attendance Percentage: " + "%.2f" + "\n", subject.attendPercent);
            System.out.print("\n");
            totalpresent += subject.presentCount;
            totalclass += subject.dayCount;

        }
        totalPercent = (totalpresent/totalclass) * 100;
        System.out.printf("Total Attendance Percentage: " + "%.2f" + "\n", totalPercent);
        System.out.print("\n");

    }
}
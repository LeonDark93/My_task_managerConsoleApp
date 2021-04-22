import java.sql.*;
import java.util.Scanner;

public class Main {

    //Подключение к базе данных MySQL (Указываем идентификатор подключения тоесть создаем константы  )

    private static final String DB_USERNAME = "root";  // Указываем пользователя БД
    private static final String DB_PASSWORD = "root";  // Указываем пароль подключения к БД
    private static final String DB_URL = "jdbc:mysql://localhost:3306/task_manager_db"; // Указываем строку подключения к базе MySQL


    public static void main(String[] args) throws Exception {
        //  Создали обьект который считывает информацию с консоли
        Scanner input = new Scanner(System.in);
        // Создаем подключения к базе данных через класс DriverManager указывая идентификаторы подключения начиная с (DB_URL,DB_USERNAME,DB_PASSWORD)
        Connection connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);

        // Создаем меню с помощью цикла (бесконечный цикл чтобы пользователь видел всегда меню)
        while (true) {
            System.out.println("1. Показать список всех задач");
            System.out.println("2. Выполнить задачу");
            System.out.println("3. Создать задачу");
            System.out.println("4. Выйти");

            // Человек будет вводить команду номер команды
            int command = input.nextInt();

            // Если человек ввел команду 1 (Хочет посмотреть список задач что храняться в БД мы должны отправить в БД запрос)
            if (command == 1) {
                // Чтобы отправить запрос в БД использум специальные конструкции для этого вызывая Statement - это обьект который умеет отправлять запросы в БД
                Statement statement = connection.createStatement();
                // Выполняем запрос котрый мы хотим отправить в БД
                String SQL_SELECT_TASKS = "SELECT * FROM task";
                // Полуаем список всех задач мы просто так не можем получить список нам нужно результат куда то положить мы используем  обьект ResultSet
                // и создали обьект result в котором будет храниться результат выполнения Запроса в БД
                // Обьект который хранит результат выполнения запроса
                //  result это обьект реализованный паттерный итератор который позволяет итерировать по строкам
                ResultSet result = statement.executeQuery(SQL_SELECT_TASKS);
                // Теперь мы хотим распечать информацию о задачах которые нах в базе
                // Пока в result что то есть за это отвечаает next() пока что есть
                while (result.next()) {
                    // Чтобы распечать вызываем обьект result и у него есть метод получения поля в зависимости какой тип данных полей в базе
                    // и по очередно вызывем обьект result и получаем те поля которые нам нужны в данном случае все
                    System.out.println(result.getInt("id_task") + " "
                            + result.getString("name") + " " + result.getString("state"));
                }
                // Если человек ввел 4 и хочет выйти мы делаем выход System.exit(0);
            } else if (command == 4) {
                System.exit(0);
                // Если человек хочет выполнить задачу т.е это задача из категории IN_PROCESS перешла в категорию DONE (нужно сделать update)
            } else if (command == 2) {
                // Мы в запросе пока не знаем какой сдесь будет id и какую задачу выполнить пользователь будет вводить id задачи потуму  и ставим что id = ?
                String sql = "UPDATE task set state = 'DONE' where id_task = ?";
                // PreparedStatement позволяеи использовать такие вопросительные знаки
                PreparedStatement preparedStatement = connection.prepareStatement(sql);
                System.out.println("Введите идентификатор задачи:");
                int taskId = input.nextInt();
                // кладем параметр котрый мы считали с консоли в строку запроса
                preparedStatement.setInt(1, taskId);
                preparedStatement.executeUpdate();

            } else if (command == 3) {
                String sql = "INSERT INTO task (name , state ) values (?,'IN_PROCESS')";
                PreparedStatement preparedStatement = connection.prepareStatement(sql);
                System.out.println("Введите название задачи:");
                input.nextLine();
                String taskName = input.nextLine();
                // Передаем строку
                preparedStatement.setString(1, taskName);
                preparedStatement.executeUpdate();
            }
            // А если что то другое ввел но мы выводим ошибку системную System.err - это поток по выводу ошибок
            else {
                System.err.println("Команда не обнаружена");
            }

        }


    }
}

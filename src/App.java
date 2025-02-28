import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Scanner;

public class App {
    public static void main(String[] args) throws Exception {
        Scanner sc = new Scanner(System.in);
        Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/ToDoList", "root", "root");
        System.out.println("Lista de Afazeres");

        try {
            while (true) {
                System.out.println("Digite:\n1 - Adicionar tarefa\n2 - Listar tarefas\n3 - Remover tarefa\n4 - Sair\n");
                int op = sc.nextInt();
                sc.nextLine(); // Limpar buffer do scanner

                switch (op) {
                    case 1:
                        System.out.println("Digite a tarefa: ");
                        String tarefa = sc.nextLine();
                        
                        PreparedStatement insertStatement = connection.prepareStatement("INSERT INTO afazeres (name) VALUES (?);");
                        insertStatement.setString(1, tarefa);
                        insertStatement.executeUpdate(); // Correção: executeUpdate() para INSERT
                        
                        System.out.println("Tarefa adicionada com sucesso!\n");
                        break;

                    case 2:
                        System.out.println("Listando tarefas...\n");
                        
                        PreparedStatement selectStatement = connection.prepareStatement("SELECT * FROM afazeres;");
                        ResultSet resultSet = selectStatement.executeQuery();
                        
                        while (resultSet.next()) {
                            System.out.println("ID: " + resultSet.getInt("id") + " | Tarefa: " + resultSet.getString("name"));
                        }
                        System.out.println();
                        break;

                    case 3:
                        System.out.println("Digite o ID da tarefa que deseja remover: ");
                        int id = sc.nextInt();
                        
                        PreparedStatement deleteStatement = connection.prepareStatement("DELETE FROM afazeres WHERE id = ?;");
                        deleteStatement.setInt(1, id);
                        int rowsAffected = deleteStatement.executeUpdate(); // Correção: executeUpdate() para DELETE
                        
                        if (rowsAffected > 0) {
                            System.out.println("Tarefa removida com sucesso!\n");
                        } else {
                            System.out.println("ID não encontrado.\n");
                        }
                        break;

                    case 4:
                        System.out.println("Saindo...");
                        connection.close(); // Fechar conexão antes de sair
                        sc.close();
                        return;

                    default:
                        System.out.println("Opção inválida! Tente novamente.\n");
                        break;
                }
            }
        } catch (Exception e) {
            System.out.println("Erro: " + e.getMessage());
        }
    }
}

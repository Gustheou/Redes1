/* ***************************************************************
* Autor............: Gustavo Pereira Nunes
* Matricula........: 202011230
* Inicio...........: 03/09/2022
* Ultima alteracao.: --/09/2022
* Nome.............: Principal
* Funcao...........: Transitar e exibir as telas. Já o programa tem a função de simular o funcionamento de um enlace de dados
*************************************************************** */

import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class Principal extends Application{
  private static Stage stage;
  private static Scene telaMenu;
  private static Scene telaSecundaria;

/* ***************************************************************
* Metodo: main
* Funcao: lançar o programa
* Parametros: args=essencial para tornar o arquivo como principal
* Retorno: void
*************************************************************** */
  public static void main (String [] args) {
    launch (args);
  }//Fim do metodo main

/* ***************************************************************
* Metodo: start
* Funcao: Iniciar a exibição de telas
* Parametros: cenario=responsavel por permitir o uso de telas
* Retorno: void
*************************************************************** */
  @Override
  public void start (Stage cenario) throws IOException {
    ControleGeral cG = new ControleGeral();
    ControleMenu cM = new ControleMenu();
    stage = cenario;
    cenario.setTitle("EnlaceDeDados");
    Parent fxmlTelaInicial = FXMLLoader.load(getClass().getResource("TelaInicial.fxml"));
    telaMenu = new Scene (fxmlTelaInicial);
    Parent fxmlTelaSecundaria = FXMLLoader.load(getClass().getResource("TelaSecundaria.fxml"));
    telaSecundaria = new Scene (fxmlTelaSecundaria);

    cenario.getIcons().add(new Image("Icon.png"));
    cenario.setScene(telaMenu);
    cenario.show();
  }//Fim do metodo start

/* ***************************************************************
* Metodo: changeScreenPrograma
* Funcao: trocar a tela inicial para a tela secundaria
* Parametros: MouseEvent event = evento que requer uma acao para ser executado, que no caso eh o de apertar um "botao"
* Retorno: void
*************************************************************** */
  public static void changeScreenPrograma (MouseEvent event) {
    stage.setScene(telaSecundaria);
  }//Fim do metodo changeScreenPrograma
}//Fim da classe Principal

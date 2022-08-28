/* ***************************************************************
* Autor............: Gustavo Pereira Nunes
* Matricula........: 202011230
* Inicio...........: 19/08/2022
* Ultima alteracao.: --/08/2022
* Nome.............: ControleGeral
* Funcao...........: Realiza o funcionamento por completo da interface (tela secundaria)
*************************************************************** */
import java.util.ArrayList;
import java.util.Random;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.MenuButton;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Line;

public class ControleGeral {

  @FXML
  private TextArea asciiTextArea, bitsTextArea, receiverTextArea, textTextArea, enquadramentoTextArea;
  @FXML
  private HBox graphicHBox;

  @FXML
  private MenuButton menuBarMenuButton;

  private String menuItem, menuEnquadroItem;
  private int codificacao, enquadramento;

/* ***************************************************************
* Metodo: binariaMenuItem
* Funcao: Definir o texto ao escolher uma codificacao
* Parametros: ActionEvent event = evento que requer uma acao para ser executado, que no caso eh o de apertar um menu item
* Retorno: void
*************************************************************** */
  @FXML
  void binariaMenuItem(ActionEvent event) {
    menuBarMenuButton.setText("Binaria");
    textTextArea.setPromptText("Digite aqui!");
  }//Fim do método binariaMenuItem
  
/* ***************************************************************
* Metodo: manchesterMenuItem
* Funcao: Definir o texto ao escolher uma codificacao
* Parametros: ActionEvent event = evento que requer uma acao para ser executado, que no caso eh o de apertar um menu item
* Retorno: void
*************************************************************** */
  @FXML
  void manchesterMenuItem(ActionEvent event) {
    menuBarMenuButton.setText("Manchester");
    textTextArea.setPromptText("Digite aqui!");
  }//Fim do método manchesterMenuItem

/* ***************************************************************
* Metodo: ManchesterDiferencialMenuItem
* Funcao: Definir o texto ao escolher uma codificacao
* Parametros: ActionEvent event = evento que requer uma acao para ser executado, que no caso eh o de apertar um menu item
* Retorno: void
*************************************************************** */
  @FXML
  void manchesterDiferencialMenuItem(ActionEvent event) {
    menuBarMenuButton.setText("Manchester D");
    textTextArea.setPromptText("Digite aqui!");
  }//Fim do método manchesterDiferencialMenuItem

/* ***************************************************************
* Metodo: AplicacaoTransmissora
* Funcao: Iniciar a transmissao, exibindo tabela bits e ascii, convertendo em fluxoDeBits e enviando como parametro para a proxima cada
* Parametros: MouseEvent event = evento que requer uma acao para ser executado, que no caso eh o de apertar um "botao" vulgo "SEND"
* Retorno: void
*************************************************************** */
  @FXML
  void AplicacaoTransmissora(MouseEvent event) {  
    setCodificacao(-1);
    menuItem = menuBarMenuButton.getText();
    menuEnquadroItem = menuBarEnquadro.getText();
    String mensagemDigitada = textTextArea.getText();
    if (menuItem.equals("Binaria")) {
      setCodificacao(0);
    } else if (menuItem.equals("Manchester")){
      setCodificacao(1);
    } else if (menuItem.equals("Manchester D")) {
      setCodificacao(2);
    } else {
      System.out.println("Selecione uma codificacao");
      menuBarMenuButton.setText("Escolha aqui");
    }
    if (menuEnquadroItem.equals("Caractere")){
      setEnquadramento(0);
    } else if (menuEnquadroItem.equals("Bytes")){
      setEnquadramento(1);
    } else if (menuEnquadroItem.equals("Bits")){
      setEnquadramento(2);
    } else if (menuEnquadroItem.equals("Fisica")){
      setEnquadramento(3);
    } else {
      System.out.println("Selecione um enquadramento");
      menuBarEnquadro.setText("Escolha aqui");
    }

    CamadaDeAplicacaoTransmissora (mensagemDigitada);
    textTextArea.setText("");

    setEnquadramento(-1);

  }//Fim do metodo AplicacaoTransmissora

/* ***************************************************************
* Metodo: CamadaDeAplicacaoTransmissora
* Funcao: Definir o texto ao escolher uma codificacao
* Parametros: String mensagem = Mensagem que foi digitada pelo usuário
* Retorno: void
*************************************************************** */
  public void CamadaDeAplicacaoTransmissora (String mensagem) {
    System.out.println();
    String ascii = mensagem;
    String bits = mensagem;
    int tipoDeCodificacao = getCodificacao();
    int tipoDeEnquadramento = getEnquadramento();
    exibirAscii(ascii);
    char[] bitsCharacter = exibirBits(bits).toCharArray();
    int[] quadro = new int [bitsCharacter.length];
    System.out.print("FluxoDeBits = ");
    for (int i = 0; i < quadro.length; i++) {
      quadro[i] = Character.getNumericValue(bitsCharacter[i]);
      System.out.print(quadro[i]);
    }
    //CamadaFisicaTransmissora(quadro, tipoDeCodificacao);
    CamadaEnlaceDadosTransmissora (quadro, tipoDeCodificacao, tipoDeEnquadramento);
  }//Fim do metodo CamadaDeAplicacaoTransmissora

/* ***************************************************************
* Metodo: CamadaFisicaTransmissora
* Funcao: Transmitir a mensagem para a codificacao selecionada
* Parametros: int[] quadro = fluxoDeBits; int tipoDeCodificacao = codificacao escolhida
* Retorno: void
*************************************************************** */
  public void CamadaFisicaTransmissora (int quadro[], int tipoDeCodificacao) {
    int fluxoBrutoDeBits [] = quadro;
    switch (tipoDeCodificacao) {
      case 0 : {//codificao binaria
        fluxoBrutoDeBits = CamadaFisicaTransmissoraCodificacaoBinaria(quadro);
        break;
      }//Fim case 0
      case 1 : {//codificacao manchester
        fluxoBrutoDeBits = CamadaFisicaTransmissoraCodificacaoManchester(quadro);
        break;
      }//Fim case 1
      case 2 : {//codificacao manchester diferencial
        fluxoBrutoDeBits = CamadaFisicaTransmissoraCodificacaoManchesterDiferencial(quadro);
        break;
      }//Fim case 2
    }//fim do switch/case
    MeioDeComunicacao(fluxoBrutoDeBits);
  }//fim do metodo CamadaFisicaTransmissora

/* ***************************************************************
* Metodo: CamadaFisicaTransmissoraCodificacaoBinaria
* Funcao: Codificar o fluxo de bits em binario
* Parametros: int[] quadro = fluxoDeBits
* Retorno: int = fluxoDeBits em binario
*************************************************************** */
  public int[] CamadaFisicaTransmissoraCodificacaoBinaria (int quadro[]){
    System.out.print("\nBinario: ");
    for (int z = 0; z<quadro.length;z++){
      System.out.print(quadro[z]);
    }
    show(quadro, graphicHBox);
    return quadro;
  }//fim do metodo CamadaFisicaTransmissoraCodificacaoBinaria

/* ***************************************************************
* Metodo: CamadaFisicaTransmissoraCodificacaoManchester
* Funcao: Codificar o fluxo de bits em manchester
* Parametros: int[] quadro = fluxoDeBits
* Retorno: int = fluxoDeBits em manchester
*************************************************************** */
  public int[] CamadaFisicaTransmissoraCodificacaoManchester (int quadro[]){
    int[] codificacaoManchester = new int[quadro.length*2];
    for (int i = 0, j = 0; i < quadro.length; i++) {
      if(quadro[i] == 0){
        codificacaoManchester[j] = 0;
        codificacaoManchester[j+1] = 1;

      }else{
        codificacaoManchester[j] = 1;
        codificacaoManchester[j+1] = 0;
      }
      j+=2;
    }//Fim do for
    System.out.print("\nManchester: ");
    for (int z = 0; z<codificacaoManchester.length;z++){
      System.out.print(codificacaoManchester[z]);
    }
    show(codificacaoManchester, graphicHBox);
    return codificacaoManchester;
  }//fim do metodo CamadaFisicaTransmissoraCodificacaoManchester

/* ***************************************************************
* Metodo: CamadaFisicaTransmissoraCodificacaoManchesterDiferencial
* Funcao: Codificar o fluxo de bits em manchester diferencial
* Parametros: int[] quadro = fluxoDeBits
* Retorno: int = fluxoDeBits em manchester diferencial
*************************************************************** */
  public int[] CamadaFisicaTransmissoraCodificacaoManchesterDiferencial (int quadro[]){
    //graphicHBox.
    int[] codificacaoManchesterDiferencial = new int[quadro.length * 2];
    for (int i = 0, j = 0; i < quadro.length; i++) {
      // Caso inicial, baixo-alto
      if (i == 0) {
        if (quadro[i] == 0) {
          codificacaoManchesterDiferencial[j] = 0;
          codificacaoManchesterDiferencial[j + 1] = 1;
        } else {
          codificacaoManchesterDiferencial[j] = 1;
          codificacaoManchesterDiferencial[j + 1] = 0;
        }
      } else {
        if (quadro[i] == quadro[i - 1]) {
          codificacaoManchesterDiferencial[j] = codificacaoManchesterDiferencial[j - 1];
          codificacaoManchesterDiferencial[j + 1] = codificacaoManchesterDiferencial[j - 2];
        } else {
          codificacaoManchesterDiferencial[j] = codificacaoManchesterDiferencial[j - 2];
          codificacaoManchesterDiferencial[j + 1] = codificacaoManchesterDiferencial[j - 1];
        } 
      }
      j += 2;
    }//Fim do for
    System.out.print("\nManchesterDiferencial: ");
    for (int z = 0; z<codificacaoManchesterDiferencial.length;z++){
      System.out.print(codificacaoManchesterDiferencial[z]);
    }
    show(codificacaoManchesterDiferencial, graphicHBox);
    return codificacaoManchesterDiferencial;
  }//fim do CamadaFisicaTransmissoraCodificacaoManchesterDiferencial

/* ***************************************************************
* Metodo: MeioDeComunicacao
* Funcao: Transferir a mesnagem de um local para o outro
* Parametros: int[] fluxoBrutoDeBits
* Retorno: void
*************************************************************** */
  public void MeioDeComunicacao (int fluxoBrutoDeBits[]){
    int[] fluxoBrutoDeBitsPontoA, fluxoBrutoDeBitsPontoB;
    fluxoBrutoDeBitsPontoA = fluxoBrutoDeBits;
    fluxoBrutoDeBitsPontoB = new int [fluxoBrutoDeBitsPontoA.length];
    int indexDoFluxoDeBits = 0;
    while (indexDoFluxoDeBits < fluxoBrutoDeBitsPontoA.length){
      fluxoBrutoDeBitsPontoB[indexDoFluxoDeBits] += fluxoBrutoDeBitsPontoA[indexDoFluxoDeBits];
      indexDoFluxoDeBits++;
    }
    CamadaFisicaReceptora(fluxoBrutoDeBitsPontoB);
  }//Fim do metodo MeioDeComunicacao

/* ***************************************************************
* Metodo: CamadaFisicaReceptora
* Funcao: Chamar a decodificacao específica
* Parametros: int[] quadro = fluxoDeBits
* Retorno: void
*************************************************************** */
  public void CamadaFisicaReceptora (int quadro[]) {
    int tipoDeDecodificacao = getCodificacao();
    int fluxoBrutoDeBits []; //ATENÇÃO: trabalhar com BITS!!!
    switch (tipoDeDecodificacao) {
      case 0 : {//Decodificao binaria
        fluxoBrutoDeBits = CamadaFisicaReceptoraDecodificacaoBinaria(quadro);
        break;
      }//Fim do case 0
      case 1 :{//Decodificacao manchester
        fluxoBrutoDeBits = CamadaFisicaReceptoraDecodificacaoManchester(quadro);
        break;
      }//Fim do case 1
      case 2 :{//Decodificacao manchester diferencial
        fluxoBrutoDeBits = CamadaFisicaReceptoraDecodificacaoManchesterDiferencial(quadro);
        break;
      }//Fim do case 2
      default: {
        fluxoBrutoDeBits = null;
      }//Fim do caso default
    }//fim do switch/case
    //chama proxima camada
    CamadaEnlaceDadosReceptora(fluxoBrutoDeBits);
  }//fim do metodo CamadaFisicaTransmissora

/* ***************************************************************
* Metodo: CamadaFisicaReceptoraDecodificacaoBinaria
* Funcao: Decodificar o fluxoDeBits em binario
* Parametros: int[] quadro = fluxoDeBits
* Retorno: int = fluxoDeBits em binario
*************************************************************** */
  public int[] CamadaFisicaReceptoraDecodificacaoBinaria (int quadro []) {
    return quadro;
  }//fim do metodo CamadaFisicaReceptoraDecodificacaoBinaria

/* ***************************************************************
* Metodo: CamadaFisicaReceptoraDecodificacaoManchester
* Funcao: Decodificar o fluxoDeBits se encontra em manchester
* Parametros: int[] quadro = fluxoDeManchester
* Retorno: int = fluxoDeBits em bits
*************************************************************** */
  public int[] CamadaFisicaReceptoraDecodificacaoManchester (int quadro []) {
    int[] decodificacaoManchester = new int[quadro.length/2];
    for (int i = 0, j = 0; i < quadro.length; i+=2) {
      if(quadro[i] == 0 && quadro[i+1] == 1){
      decodificacaoManchester[j] = 0;
      }
      if(quadro[i] == 1 && quadro[i+1] == 0){
        decodificacaoManchester[j] = 1;
      }
    j++;
    }
    return decodificacaoManchester;
  }//fim do metodo CamadaFisicaReceptoraDecodificacaoManchester

/* ***************************************************************
* Metodo: CamadaFisicaReceptoraDecodificacaoManchesterDiferencial
* Funcao: Decodificar o fluxoDeBits que se encontra em manchester diferencial
* Parametros: int[] quadro = fluxoDeManchesterDiferencial
* Retorno: int = fluxoDeBits em bits
*************************************************************** */
  public int[] CamadaFisicaReceptoraDecodificacaoManchesterDiferencial(int quadro[]){
    int[] decodificacaoManchesterDiferencial = new int[quadro.length / 2];
    for (int i = 0, j = 0; i < quadro.length; i += 2) {
      if (i == 0) {
        if (quadro[i] == 0 && quadro[i + 1] == 1) {
          decodificacaoManchesterDiferencial[0] = 0;
        }
        if (quadro[i] == 1 && quadro[i + 1] == 0) {
          decodificacaoManchesterDiferencial[0] = 1;
        }
      } else {
        if (quadro[i] == quadro[i - 1]) {
          decodificacaoManchesterDiferencial[j] = decodificacaoManchesterDiferencial[j - 1];
        } else {
          if (decodificacaoManchesterDiferencial[j - 1] == 1) {
            decodificacaoManchesterDiferencial[j] = 0;
          } else {
            decodificacaoManchesterDiferencial[j] = 1;
          }
        }
      }
      j++;
    }
  return decodificacaoManchesterDiferencial;
  }//fim do CamadaFisicaReceptoraDecodificacaoManchesterDiferencial

/* ***************************************************************
* Metodo: CamadaDeAplicacaoReceptora
* Funcao: Pegar o fluxoDeBits e transformar em String
* Parametros: int[] quadro = fluxoDeBits
* Retorno: void
*************************************************************** */
  public void CamadaDeAplicacaoReceptora (int quadro []) {
    String mensagem = bitsToString(quadro);
    System.out.println("\n\nResultado final: ");
    for (int i = 0; i < quadro.length; i++){
      System.out.print(quadro[i]);
    }
    //chama proxima camada
    AplicacaoReceptora(mensagem);
  }//fim do metodo CamadaDeAplicacaoReceptora

/* ***************************************************************
* Metodo: AplicacaoReceptora
* Funcao: Exibir a mensagem decodificada
* Parametros: String mensagem = mensagem decodificada
* Retorno: void
*************************************************************** */
  public void AplicacaoReceptora (String mensagem) {
    receiverTextArea.setText(mensagem);
  }//fim do metodo AplicacaoReceptora

/* ***************************************************************
* Metodo: exibirAscii
* Funcao: pegar a mensagem digitada, transformar em ascii e exibir-la
* Parametros: String mensagemDigitada = mensagem digitada pelo usuário
* Retorno: void
*************************************************************** */
  public void exibirAscii(String mensagemDigitada){
    char[] mensagem = mensagemDigitada.toCharArray();
    StringBuilder resultadoAscii = new StringBuilder();
    for (char mensagemChar : mensagem) {
      resultadoAscii.append(mensagemChar+" = " + Integer.toString(mensagemChar)+"\n");
      asciiTextArea.setText(resultadoAscii.toString());
    }
  }//fim do metodo exibirAscii
  
/* ***************************************************************
* Metodo: exibirBits
* Funcao: Pegar a mensagem digitada, transformar em bits e exibir-la
* Parametros: String mensagemDigitada = mensagem digitada pelo usuario
* Retorno: String = fluxoDeBits
*************************************************************** */
  public String exibirBits (String mensagemDigitada) {
    StringBuilder resultadoBits = new StringBuilder();
    StringBuilder mensagemEmBits = new StringBuilder();
    char[] mensagem = mensagemDigitada.toCharArray();
    for (char mensagemChar : mensagem) {
      resultadoBits.append(mensagemChar+" = "+String.format("%8s", Integer.toBinaryString(mensagemChar)).replaceAll(" ", "0")+"\n");
      mensagemEmBits.append(String.format("%8s", Integer.toBinaryString(mensagemChar)).replaceAll(" ", "0"));
      bitsTextArea.setText(resultadoBits.toString());
    }
    return mensagemEmBits.toString();
  }//Fim do exibirBits

/* ***************************************************************
* Metodo: binaryToDecimal
* Funcao: Pega um numero binario e o transforma para decimal
* Parametros: String numero = fluxoDeBits
* Retorno: int valorDecimal = valor convertido de bits para decimal
*************************************************************** */
  public static int binaryToDecimal(String numero) {
    String number = numero;
    // guarda o valor decimal
    int valorDecimal = 0;
    // Iniciando o valor base como 1
    int base = 1;
    for (int i = number.length() - 1; i >= 0; i--) {
      // Se o bit atual for 1
      if (number.charAt(i) == '1'){
        valorDecimal += base;
      }
      base = base * 2;
    }
    return valorDecimal;
  }//Fim do metodo binaryToDecimal

/* ***************************************************************
* Metodo: setCodificacao
* Funcao: guardar a codificacao escolhoda
* Parametros: int codificacao = numero que representa a codificacao escolhoda
* Retorno: void
*************************************************************** */
  public void setCodificacao(int codificacao) {
    this.codificacao = codificacao;
  }//Fim do metodo setCodificacao
  
/* ***************************************************************
* Metodo: getCodificacao
* Funcao: recuperar a codificacao escolhida
* Parametros: void
* Retorno: int codificacao = codificacao escolhida
*************************************************************** */
  public int getCodificacao() {
    return codificacao;
  }//Fim do metodo getCodigicacao


/* ***************************************************************
* Metodo: addSinal
* Funcao: adicionar um line no display
* Parametros: HBox display= box referente ao gráfico, int current = bit atual, int prev = bit anterior
* Retorno: void
*************************************************************** */
  private static void addSinal(HBox display, int current, int prev) {
    ObservableList led = display.getChildren();
    VBox vBox = new VBox();
    if (current == 0){
      vBox.setAlignment(Pos.BOTTOM_LEFT);
    }else {
      vBox.setAlignment(Pos.TOP_LEFT);
    }//Fim do if-else
    if (current != prev && !led.isEmpty()){
      led.add(0, new Line(0, 0, 0,display.getHeight() - display.getPadding().getTop() - display.getPadding().getBottom() - 1));
    }//Fim do if
    vBox.getChildren().add(new Line(0, 0, 50, 0));
    led.add(0, vBox);
  }//Fim do metodo addSinal

/* ***************************************************************
* Metodo: show
* Funcao: mostrar o display
* Parametros: HBox display= box referente ao gráfico, int[] bits = fluxo de bits do procedimento selecionado
* Retorno: void
*************************************************************** */
  public static void show(int[] bits, HBox display) {
    for (int i = bits.length - 1; i >= 0; i--) {
      if (i == bits.length - 1){
        addSinal(display, bits[i], 'n');
      }else{
        addSinal(display, bits[i], bits[i + 1]);
      }//fim do if-else
    }//Fim do for
  }//Fim do metodo show

  public String bitsToString(int[] quadro) {
    String mensagem = "";
    String letra = "";
    int contador = 0;
    for (int i = 0; i < quadro.length; i++){
      letra += quadro[i];
      if (contador == 7) {
        mensagem += (char) binaryToDecimal(letra);
        letra = "";
        contador = -1;
      }
      contador ++;
    }
    return mensagem;
  }
  
  //------Enlace de dados-------//
  
  @FXML
  private MenuButton menuBarEnquadro;
  
  @FXML
  public void caractereMenuItem(ActionEvent event) {
    menuBarEnquadro.setText("Caractere");
    textTextArea.setPromptText("Digite aqui!");
  }

  @FXML
  public void bytesMenuItem(ActionEvent event) {
    menuBarEnquadro.setText("Bytes");
    textTextArea.setPromptText("Digite aqui!");
  }

  @FXML
  public void bitsMenuItem(ActionEvent event) {
    menuBarEnquadro.setText("Bits");
    textTextArea.setPromptText("Digite aqui!");
  }

  @FXML
  public void fisicaMenuItem(ActionEvent event) {
    menuBarEnquadro.setText("Fisica");
    textTextArea.setPromptText("Digite aqui!");
  }

  public void setEnquadramento (int enquadramento){
    this.enquadramento = enquadramento;
  }

  public int getEnquadramento() {
    return enquadramento;
  }

  public void CamadaEnlaceDadosTransmissora (int quadro[], int tipoDeCodificacao, int tipoDeEnquadramento){
    CamadaDeEnlaceDadosTransmissoraEnquadramento(quadro, tipoDeEnquadramento);  
    //CamadaFisicaTransmissora(quadro, tipoDeCodificacao);
  }
  
  public void CamadaDeEnlaceDadosTransmissoraEnquadramento (int quadro[], int enquadramento){
    int [] quadroEnquadrado = quadro;
    switch (enquadramento){
      case 0: {//Contagem de caracteres
        quadroEnquadrado = CamadaEnlaceTransmissoraEnquadramentoContagemDeCaracteres(quadro);
        break;
      }//Fim case 0
      case 1: {//Insercao de bytes
        quadroEnquadrado = CamadaEnlaceDadosTransmissoraEnquadramentoInsercaoDeBytes(quadro);
        break;
      }//Fim case 1
      case 2: {//Insercao de bits
        quadroEnquadrado = CamadaEnlaceDadosTransmissoraEnquadramentoInsercaoDeBits(quadro);
        break;
      }//Fim case 2
      case 3: {//Violacao da camada fisica
        quadroEnquadrado = CamadaEnlaceDadosTransmissoraEnquadramentoViolacaoDaCamadaFisica(quadro);
        break;
      }//Fim case 3
    }//Fim do switch case
    int tipoDeCodificacao = getCodificacao();
    CamadaFisicaTransmissora(quadroEnquadrado, tipoDeCodificacao);
  }//Fim do metodo CmadaDeEnlaceDadosTransmissoraEnquadramento
  
  public int[] CamadaEnlaceTransmissoraEnquadramentoContagemDeCaracteres (int quadro[]){
    ArrayList quadroComContagemDeCaracteres = new ArrayList<>();
    if (getCodificacao() == 0){  
      for (int i = 0; i < quadro.length; i++) {
        quadroComContagemDeCaracteres.add(quadro[i]);
      }
      Random gerador = new Random();
      int tamanhoDoFluxoDeBits = quadro.length;
      int index = 0;
      int numeroGerado = 0;
      while(index < tamanhoDoFluxoDeBits){
        numeroGerado = (gerador.nextInt(tamanhoDoFluxoDeBits-index));
        if (numeroGerado < 0){
          numeroGerado *= -1;
        } else if (numeroGerado == 0) {
          numeroGerado+=2;
        } else if (numeroGerado == 1) {
          numeroGerado+=1;
        }
        quadroComContagemDeCaracteres.add(index, numeroGerado);
        index+=numeroGerado;
        tamanhoDoFluxoDeBits++;
      }
      int[] quadroEnquadradoContagemDeCaracteres = new int [quadroComContagemDeCaracteres.size()];
      System.out.println("\nNovo quadro com contagem:");
      for (int i = 0; i < quadroComContagemDeCaracteres.size();i++) {
        quadroEnquadradoContagemDeCaracteres[i] = (int)quadroComContagemDeCaracteres.get(i);
        System.out.print(quadroEnquadradoContagemDeCaracteres[i]);
      }
      return quadroEnquadradoContagemDeCaracteres;
    }
    /*
    //Dividir em partes
    ArrayList quandroEnquadrado = new ArrayList<>();
    int contador = 0;
    int indexNewPackage = 0;
    for (int i = 0; i < numerosUsadoParaContagem.size(); i++){
      for (int j = 0; j < quadroComContagemDeCaracteres.size(); j++) {
        if ((int) numerosUsadoParaContagem.get(i) == quadroEnquadradoContagemDeCaracteres[j]) {
          contador = j;
          ArrayList pacote = new ArrayList<>();
          while ((int)numerosUsadoParaContagem.get(i+1) != quadroEnquadradoContagemDeCaracteres[contador]){
            quandroEnquadrado.add(indexNewPackage, quadroEnquadradoContagemDeCaracteres[contador]);
            contador++;
          }
          indexNewPackage++;
        }
      }
    }
    return quadroEnquadradoContagemDeCaracteres;
    */
    exibirEnquadramentoContagemCaracteres(quadro);
    return quadro;
  }//Fim do metodo CamadaEnlaceTransmissoraEnquadramentoContagemDeCaracteres
  
  public void exibirEnquadramentoContagemCaracteres(int[] quadro){
    String mensagem = bitsToString(quadro);
    ArrayList quadroComContagemDeCaracteres = new ArrayList<>();
    for (int i = 0; i < mensagem.length(); i++) {
      quadroComContagemDeCaracteres.add(mensagem.charAt(i));
    }
    Random gerador = new Random();
    int tamanhoDoFluxoDeBits = mensagem.length();
    int index = 0;
    int numeroGerado = 0;
    while(index < tamanhoDoFluxoDeBits){
      numeroGerado = (gerador.nextInt(tamanhoDoFluxoDeBits-index));
      if (numeroGerado < 0){
        numeroGerado *= -1;
      } else if (numeroGerado == 0) {
        numeroGerado+=2;
      } else if (numeroGerado == 1) {
        numeroGerado+=1;
      }
      quadroComContagemDeCaracteres.add(index, "["+numeroGerado+"]");
      index+=numeroGerado;
      tamanhoDoFluxoDeBits++;
    }
    StringBuilder resultado = new StringBuilder();
    for (int i = 0; i < quadroComContagemDeCaracteres.size(); i++) {
      resultado.append(quadroComContagemDeCaracteres.get(i));    
    }
    enquadramentoTextArea.setText(String.valueOf(resultado));
  }

  public int[] CamadaEnlaceDadosTransmissoraEnquadramentoInsercaoDeBytes (int quadro[]){
    //Inserção do ç como flag
    ArrayList newPackage = new ArrayList<>();
    for (int i = 0; i < quadro.length; i++){
      newPackage.add(i, quadro[i]);
    }
    if (getCodificacao() == 0){
      int tamanhoDoFluxoDeBits = quadro.length;
      int index = 4;//Número para definir o tamanho do quadro
      char flag = 'ç';
      newPackage.add(0,flag);
      while(index < tamanhoDoFluxoDeBits){
        if (index % 4 == 0){
          newPackage.add(index, flag);
          newPackage.add(index+1,flag);
          index+=4;
        } else {
          newPackage.add(index%4,flag);
          newPackage.add((index%4) + 1,flag);
          index+=index%4;
        }
        tamanhoDoFluxoDeBits++;
      }
      newPackage.add(flag);
      System.out.println("\nInsercao de byte: "+ newPackage);
      int[] quadroEnquadradoInsercaoDeBytes = new int [newPackage.size()];
      for (int i = 0; i < newPackage.size();i++) {
        if (newPackage.get(i).equals(flag)) {
          newPackage.remove(i);
          newPackage.add(i, 11100111);
        }
        quadroEnquadradoInsercaoDeBytes[i] = (int)newPackage.get(i);
      }
      return quadroEnquadradoInsercaoDeBytes;
    }
    

    /*
    //dividir para conquistar

    int[] quadroFinal = new int [newPackage.size()];
    int separador = 0;
    for (int i = 0; i < newPackage.size(); i++) {
      ArrayList quadroSeparado = new ArrayList<>();
      for (int j = 0; j < newPackage.size();j++){
        if (newPackage.get(j).equals('ç')) {
          separador++;
          quadroSeparado.add(newPackage);
        }
        if (separador == 2) {
          separador = 0;
          quadroFinal[i] = quadroSeparado;
        }
      }
    }
    */
    exibirEnquadramentoInsercaoDeBytes(quadro);
    return quadro;
  }//Fim do metodo CamadaEnlaceDadosTransmissoraEnquadramentoInsercaoDeBytes
  
  public void exibirEnquadramentoInsercaoDeBytes(int[] quadro) {
    String mensagem = bitsToString(quadro);
    ArrayList newPackage = new ArrayList<>();
    for (int i = 0; i < mensagem.length(); i++){
      newPackage.add(i, mensagem.charAt(i));
    }
    int tamanhoDoFluxoDeBits = mensagem.length();
    int index = 4;//Número para definir o tamanho do quadro
    char flag = 'ç';
    newPackage.add(0,flag);
    while(index < tamanhoDoFluxoDeBits){
      if (index % 4 == 0){
        newPackage.add(index, flag);
        newPackage.add(index+1,flag);
        index+=4;
      } else {
        newPackage.add(index%4,flag);
        newPackage.add((index%4) + 1,flag);
        index+=index%4;
      }
      tamanhoDoFluxoDeBits++;
    }
    newPackage.add(flag);
    
    StringBuilder resultado = new StringBuilder();
    for (int i = 0; i < newPackage.size(); i++) {
      resultado.append(newPackage.get(i));    
    }
    enquadramentoTextArea.setText(String.valueOf(resultado));
  }

  public int[] CamadaEnlaceDadosTransmissoraEnquadramentoInsercaoDeBits (int quadro[]){
    ArrayList newPackage = new ArrayList<>();
    for (int i = 0; i < quadro.length; i++){
      newPackage.add(i, quadro[i]);
    }
      int tamanhoDoFluxoDeBits = quadro.length;
      int index = 4;//Número para definir o tamanho do quadro
      int flag = 0111110;
      newPackage.add(0,flag);
      while(index < tamanhoDoFluxoDeBits){
        if (index % 4 == 0){
          newPackage.add(index, flag);
          newPackage.add(index+1,flag);
          index+=4;
        } else {
          newPackage.add(index%4,flag);
          newPackage.add((index%4) + 1,flag);
          index+=index%4;
        }
        tamanhoDoFluxoDeBits++;
      }
      newPackage.add(flag);
      System.out.println("Insercao de bits: "+ newPackage);
      int[] quadroEnquadradoInsercaoDeBits = new int [newPackage.size()];
      for (int i = 0; i < newPackage.size();i++) {
        quadroEnquadradoInsercaoDeBits[i] = (int)newPackage.get(i);
        System.out.print(quadroEnquadradoInsercaoDeBits[i]);
      }

    exibirEnquadramentoInsercaoDeBits(quadro);
    return quadro;
  }//Fim do metodo CamadaEnlaceDadosTransmissoraEnquadramentoInsercaoDeBits
  
  public void exibirEnquadramentoInsercaoDeBits (int[] quadro) {
    String mensagem = bitsToString(quadro);
    ArrayList newPackage = new ArrayList<>();
    for (int i = 0; i < mensagem.length(); i++){
      newPackage.add(i, mensagem.charAt(i));
    }
    int tamanhoDoFluxoDeBits = mensagem.length();
    int index = 4;//Número para definir o tamanho do quadro
    String flag = "0111110";
    newPackage.add(0,flag);
    while(index < tamanhoDoFluxoDeBits){
      if (index % 4 == 0){
        newPackage.add(index, flag);
        newPackage.add(index+1,flag);
        index+=4;
      } else {
        newPackage.add(index%4,flag);
        newPackage.add((index%4) + 1,flag);
        index+=index%4;
      }
      tamanhoDoFluxoDeBits++;
    }
    newPackage.add(flag);
    
    StringBuilder resultado = new StringBuilder();
    for (int i = 0; i < newPackage.size(); i++) {
      resultado.append(newPackage.get(i));    
    }
    enquadramentoTextArea.setText(String.valueOf(resultado));
  }
  

  public int[] CamadaEnlaceDadosTransmissoraEnquadramentoViolacaoDaCamadaFisica (int quadro[]){
    int sinais = quadro.length / 10;
    int[] quadroVioladoFisicamente = new int[quadro.length + (sinais * 2)];
    for (int i = 0, j = 0; i < quadro.length; i++, j++) {
      if (i % 10 == 0) {
        quadroVioladoFisicamente[j] = 1;
        quadroVioladoFisicamente[j + 1] = 1;
        j++;
      }
      quadroVioladoFisicamente[j] = quadro[i];
    }
    return quadro;
  }//Fim do metodo CamadaEnlaceDadosTransmissoraEnquadramentoViolacaoDaCamadaFisicax
  
  public void CamadaEnlaceDadosReceptora (int quadro[]){
    int enquadramento = getEnquadramento();
    CamadaEnlaceDadosReceptoraEnquadramento(quadro, enquadramento);
    //CamadaDeAplicacaoReceptora(quadro);
  }//Fim do metodo CamadaEnlaceDadosReceptora
  
  public void CamadaEnlaceDadosReceptoraEnquadramento (int quadro[], int enquadramento){
    int quadroDesenquadrado[] = quadro;
    switch (enquadramento){
      case 0: {//Contagem de caracteres
        quadroDesenquadrado = CamadaEnlaceDadosReceptoraEnquadramentoContagemDeCaracteres(quadro);
        break;
      }
      case 1: {
        quadroDesenquadrado = CamadaEnlaceDadosReceptoraEnquadramentoInsercaoDeBytes(quadro);
        break;
      }
      case 2: {
        quadroDesenquadrado = CamadaEnlaceDadosReceptoraEnquadramentoInsercaoDeBits(quadro);
        break;
      }
      case 3: {
        quadroDesenquadrado = CamadaEnlaceDadosReceptoraEnquadramentoViolacaoDaCamadaFisica(quadro);
        break;
      }
    }//Fim do switch-case
    CamadaDeAplicacaoReceptora(quadroDesenquadrado);
  }//Fim do metodo CamadaEnlaceDadosReceptoraEnquadramento
  
  public int[] CamadaEnlaceDadosReceptoraEnquadramentoContagemDeCaracteres (int quadro[]){
    ArrayList desenquadrarContagemDeCaractereArray = new ArrayList<>();
    for (int i = 0; i < quadro.length; i++) {//Colocar o vetor em um array para facilitar a remocao
      desenquadrarContagemDeCaractereArray.add(i, quadro[i]);
    }
    for (int j = 0; j < desenquadrarContagemDeCaractereArray.size();j++) {
      if ((int)desenquadrarContagemDeCaractereArray.get(j) > 1){
        desenquadrarContagemDeCaractereArray.remove(j);
        j--;
      }
    }
    int[] desenquadradoContagemDeCaractere = new int[desenquadrarContagemDeCaractereArray.size()];
    for (int z = 0; z < desenquadradoContagemDeCaractere.length; z++) {
      desenquadradoContagemDeCaractere[z] = (int) desenquadrarContagemDeCaractereArray.get(z);
    } 
    return desenquadradoContagemDeCaractere;
  }//Fim do metodo CamadaEnlaceDadosReceptoraEnquadramentoContagemDeCaracteres
  
  public int[] CamadaEnlaceDadosReceptoraEnquadramentoInsercaoDeBytes (int quadro[]){
    ArrayList desenquadrarContagemDeBytesArray = new ArrayList<>();
    for (int i = 0; i < quadro.length; i++) {
      desenquadrarContagemDeBytesArray.add(i, quadro[i]);
    }
    for (int j = 0; j<desenquadrarContagemDeBytesArray.size();j++) {
      if ((int) desenquadrarContagemDeBytesArray.get(j) == 11100111){
        desenquadrarContagemDeBytesArray.remove(j);
        j--;
      }
    }
    int[] desenquadradoInsercaoDeBytes = new int [desenquadrarContagemDeBytesArray.size()];
    for (int z = 0; z < desenquadrarContagemDeBytesArray.size();z++){
      desenquadradoInsercaoDeBytes[z] = (int)desenquadrarContagemDeBytesArray.get(z);
    }
    return desenquadradoInsercaoDeBytes;
  }//Fim do metodo CamadaEnlaceDadosReceptoraEnquadramentoInsercaoDeBytes
  
  public int[] CamadaEnlaceDadosReceptoraEnquadramentoInsercaoDeBits (int quadro[]){
    ArrayList desenquadrarInsercaoDeBitsArray = new ArrayList<>();
    for (int i = 0; i < quadro.length; i++) {
      desenquadrarInsercaoDeBitsArray.add(i, quadro[i]);
    }
    for (int j = 0; j<desenquadrarInsercaoDeBitsArray.size();j++) {
      if ((int) desenquadrarInsercaoDeBitsArray.get(j) == 0111110){
        desenquadrarInsercaoDeBitsArray.remove(j);
        j--;
      }
    }
    int[] desenquadradoInsercaoDeBits = new int [desenquadrarInsercaoDeBitsArray.size()];
    for (int z = 0; z < desenquadrarInsercaoDeBitsArray.size();z++){
      desenquadradoInsercaoDeBits[z] = (int)desenquadrarInsercaoDeBitsArray.get(z);
    }
    return desenquadradoInsercaoDeBits;
  }//Fim do metodo CamadaEnlaceDadosReceptoraEnquadramentoInsercaoDeBits
  
  public int[] CamadaEnlaceDadosReceptoraEnquadramentoViolacaoDaCamadaFisica(int quadro[]){
    int sinais = quadro.length / 10;
    int[] quadroVioladoFisicamente = new int[quadro.length - (sinais * 2)];
    for (int i = 0, j = 0; i < quadro.length; i++, j++) {
      if (i % 10 == 0) {
        i += 2;
      }
      quadroVioladoFisicamente[j] = quadro[i];
    }
    return quadro;
  }//Fim do metodo CamadaEnlaceDadosReceptoraEnquadramentoViolacaoDaCamadaFisica
  
}//Fim da classe ControleGeral

/* ***************************************************************
* Autor............: Gustavo Pereira Nunes
* Matricula........: 202011230
* Inicio...........: 19/08/2022
* Ultima alteracao.: --/08/2022
* Nome.............: ControleGeral
* Funcao...........: Realiza o funcionamento por completo da interface (tela secundaria)
*************************************************************** */
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
  private TextArea asciiTextArea, bitsTextArea, receiverTextArea, textTextArea;
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
          //show(codificacaoManchesterDiferencial, graphicHBox);
        } else {
          codificacaoManchesterDiferencial[j] = 1;
          codificacaoManchesterDiferencial[j + 1] = 0;
          //show(codificacaoManchesterDiferencial, graphicHBox);
        }
      } else {
        if (quadro[i] == quadro[i - 1]) {
          codificacaoManchesterDiferencial[j] = codificacaoManchesterDiferencial[j - 1];
          codificacaoManchesterDiferencial[j + 1] = codificacaoManchesterDiferencial[j - 2];
          //show(codificacaoManchesterDiferencial, graphicHBox);
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
    CamadaDeAplicacaoReceptora(fluxoBrutoDeBits);
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
    CamadaFisicaTransmissora(quadro, tipoDeCodificacao);
  }
  
  public void CamadaDeEnlaceDadosTransmissoraEnquadramento (int quadro[], int enquadramento){
    int tipoDeEnquadramento = enquadramento;
    int [] quadroEnquadrado;
    switch (tipoDeEnquadramento){
      case 0: {//Contagem de caracteres
        quadroEnquadrado = CamadaEnlaceTransmissoraEnquadramentoContagemDeCaracteres(quadro);
        break;
      }
      case 1: {//Insercao de bytes
        quadroEnquadrado = CamadaEnlaceDadosTransmissoraEnquadramentoInsercaoDeBytes(quadro);
        break;
      }
      case 2: {//Insercao de bits
        quadroEnquadrado = CamadaEnlaceDadosTransmissoraEnquadramentoInsercaoDeBits(quadro);
        break;
      }
      case 3: {//Violacao da camada fisica
        quadroEnquadrado = CamadaEnlaceDadosTransmissoraEnquadramentoViolacaoDaCamadaFisica(quadro);
        break;
      }
    }
  }//Fim do metodo CmadaDeEnlaceDadosTransmissoraEnquadramento
  
  public int[] CamadaEnlaceTransmissoraEnquadramentoContagemDeCaracteres (int quadro[]){
  
    return quadro;
  }//Fim do metodo CamadaEnlaceTransmissoraEnquadramentoContagemDeCaracteres
  
  public int[] CamadaEnlaceDadosTransmissoraEnquadramentoInsercaoDeBytes (int quadro[]){
  
    return quadro;
  }//Fim do metodo CamadaEnlaceDadosTransmissoraEnquadramentoInsercaoDeBytes
  
  public int[] CamadaEnlaceDadosTransmissoraEnquadramentoInsercaoDeBits (int quadro[]){
  
    return quadro;
  }//Fim do metodo CamadaEnlaceDadosTransmissoraEnquadramentoInsercaoDeBits
  
  public int[] CamadaEnlaceDadosTransmissoraEnquadramentoViolacaoDaCamadaFisica (int quadro[]){
  
    return quadro;
  }//Fim do metodo CamadaEnlaceDadosTransmissoraEnquadramentoViolacaoDaCamadaFisicax
  
  public void CamadaEnlaceDadosReceptora (int quadro[]){
    CamadaEnlaceDadosReceptoraEnquadramento(quadro);
    CamadaDeAplicacaoReceptora(quadro);
  }//Fim do metodo CamadaEnlaceDadosReceptora
  
  public void CamadaEnlaceDadosReceptoraEnquadramento (int quadro[]){
    int tipoDeEnquadramento = getEnquadramento();
    int quadroDesenquadrado[];
    switch (tipoDeEnquadramento){
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
  }//Fim do metodo CamadaEnlaceDadosReceptoraEnquadramento
  
  public int[] CamadaEnlaceDadosReceptoraEnquadramentoContagemDeCaracteres (int quadro[]){
  
    return quadro;
  }//Fim do metodo CamadaEnlaceDadosReceptoraEnquadramentoContagemDeCaracteres
  
  public int[] CamadaEnlaceDadosReceptoraEnquadramentoInsercaoDeBytes (int quadro[]){
  
    return quadro;
  }//Fim do metodo CamadaEnlaceDadosReceptoraEnquadramentoInsercaoDeBytes
  
  public int[] CamadaEnlaceDadosReceptoraEnquadramentoInsercaoDeBits (int quadro[]){
  
    return quadro;
  }//Fim do metodo CamadaEnlaceDadosReceptoraEnquadramentoInsercaoDeBits
  
  public int[] CamadaEnlaceDadosReceptoraEnquadramentoViolacaoDaCamadaFisica(int quadro[]){
  
    return quadro;
  }//Fim do metodo CamadaEnlaceDadosReceptoraEnquadramentoViolacaoDaCamadaFisica
  
}//Fim da classe ControleGeral

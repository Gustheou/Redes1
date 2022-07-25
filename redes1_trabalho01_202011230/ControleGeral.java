import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.MenuButton;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseEvent;

public class ControleGeral {

  @FXML
  private TextArea asciiTextArea, bitsTextArea, receiverTextArea, textTextArea, graphicTextArea;

  @FXML
  private MenuButton menuBarMenuButton;

  private String mensagem, menuItem, mensagemCodificada;
  private int codificacao;

  
  @FXML
  void binariaMenuItem(ActionEvent event) {
    menuBarMenuButton.setText("Binaria");
  }

  @FXML
  void manchesterDiferencialMenuItem(ActionEvent event) {
    menuBarMenuButton.setText("Manchester D");
  }

  @FXML
  void manchesterMenuItem(ActionEvent event) {
    menuBarMenuButton.setText("Manchester");
  }

  @FXML
  void AplicacaoTransmissora(MouseEvent event) {  
    menuItem = menuBarMenuButton.getText();
    String mensagemDigitada = textTextArea.getText();
    setMensagem (mensagemDigitada);
    //asciiTextArea.setText("");
    //bitsTextArea.setText("");
    //receiverTextArea.setText("");
    //graphicTextArea.setText("");
    /*if (!menuItem.equals("Binaria") && !menuItem.equals("Manchester") && !menuItem.equals("Manchester D")){
      System.out.println("Selecione uma codificacao");
      menuBarMenuButton.setText("Escolha aqui");
    }*/
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
    CamadaDeAplicacaoTransmissora (mensagemDigitada);
    textTextArea.setText("");
  }

  public void CamadaDeAplicacaoTransmissora (String mensagem) {
    String ascii = mensagem;
    String bits = mensagem;
    exibirAscii(ascii);
    exibirBits(bits);
    int tipoDeCodificacao = getCodificacao();
    char[] mensagemTamanho = mensagem.toCharArray();
    int[] quadro = new int [mensagemTamanho.length];
    char[] bitsCharacter = getMensagemCodificada().toCharArray();
    for (int i = 0; i < quadro.length; i++) {
      quadro[i] = Character.getNumericValue(bitsCharacter[i]);
    }
    CamadaFisicaTransmissora (quadro, tipoDeCodificacao);
  }//Fim do metodo CamadaDeAplicacaoTransmissora

  public void CamadaFisicaTransmissora (int quadro[], int tipoDeCodificacao) {
    int fluxoBrutoDeBits [] = quadro; //ATENÇÃO: trabalhar com BITS!!!
    switch (tipoDeCodificacao) {
    case 0 : {//codificao binaria
      fluxoBrutoDeBits = CamadaFisicaTransmissoraCodificacaoBinaria(quadro);
      break;
    }
    case 1 : {//codificacao manchester
      fluxoBrutoDeBits = CamadaFisicaTransmissoraCodificacaoManchester(quadro);
      break;
    }
    case 2 : //codificacao manchester diferencial
      fluxoBrutoDeBits = CamadaFisicaTransmissoraCodificacaoManchesterDiferencial(quadro);
      break;
    }//fim do switch/case
    MeioDeComunicacao(fluxoBrutoDeBits);
  }//fim da classe CamadaFisicaTransmissora

  public int[] CamadaFisicaTransmissoraCodificacaoBinaria (int quadro[]){
    return quadro;
  }//fim do metodo CamadaFisicaTransmissoraCodificacaoBinaria

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
    }
    return codificacaoManchester;
  }//fim do metodo CamadaFisicaTransmissoraCodificacaoManchester

  public int[] CamadaFisicaTransmissoraCodificacaoManchesterDiferencial (int quadro[]){
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
    }
    return codificacaoManchesterDiferencial;
    
  }//fim do CamadaFisicaTransmissoraCodificacaoManchesterDiferencial

  public void MeioDeComunicacao (int fluxoBrutoDeBits[]){
    int[] transmissor = fluxoBrutoDeBits;
    int tamanho = transmissor.length;
    int[] receptor = new int [tamanho];
    for (int i = 0; i < tamanho; i++) {
      receptor[i] = transmissor[i];
    }
    CamadaFisicaReceptora(fluxoBrutoDeBits);
  }

  public void CamadaFisicaReceptora (int quadro[]) {
    int tipoDeDecodificacao = getCodificacao();
    int fluxoBrutoDeBits []; //ATENÇÃO: trabalhar com BITS!!!
    switch (tipoDeDecodificacao) {
      case 0 : {//codificao binaria
        fluxoBrutoDeBits = CamadaFisicaReceptoraDecodificacaoBinaria(quadro);
        break;
      }case 1 :{//codificacao manchester
        fluxoBrutoDeBits = CamadaFisicaReceptoraDecodificacaoManchester(quadro);
        break;
      }case 2 :{//codificacao manchester diferencial
        fluxoBrutoDeBits = CamadaFisicaReceptoraDecodificacaoManchesterDiferencial(quadro);
        break;
      }default: {
        fluxoBrutoDeBits = null;
      }
    }//fim do switch/case
    //chama proxima camada
    CamadaDeAplicacaoReceptora(fluxoBrutoDeBits);
  }//fim do metodo CamadaFisicaTransmissora

  int[] CamadaFisicaReceptoraDecodificacaoBinaria (int quadro []) {
    //implementacao do algoritmo para DECODIFICAR
    return quadro;
  }//fim do metodo CamadaFisicaReceptoraDecodificacaoBinaria

  int[] CamadaFisicaReceptoraDecodificacaoManchester (int quadro []) {
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

  int[] CamadaFisicaReceptoraDecodificacaoManchesterDiferencial(int quadro[]){
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

  public void CamadaDeAplicacaoReceptora (int quadro []) {
    String mensagem = "";
    String letra = "";
    for (int i = 0; i < quadro.length; i++) {
      letra += quadro[i];
      if(letra.length() % 7 == 0){
        int ascii = Integer.parseInt(letra,2);
        mensagem += ((char) ascii);
        letra = "";
      }
    }
    //chama proxima camada
    AplicacaoReceptora(mensagem);
  }//fim do metodo CamadaDeAplicacaoReceptora

  public void AplicacaoReceptora (String mensagem) {
    receiverTextArea.setText(mensagem);
  }//fim do metodo AplicacaoReceptora

  public void exibirAscii(String mensagemDigitada){
    char[] mensagem = mensagemDigitada.toCharArray();
    //char[] mensagem = textTextArea.getText().toCharArray();
    /*int[] mensagemAscii = new int[mensagem.length];
    for(int i = 0;i < mensagem.length;i++){
      mensagemAscii[i] = mensagem[i];
      asciiTextArea.setText(asciiTextArea.getText() + mensagem[i] + " = " + mensagemAscii[i] + "\n");
    }*/
    StringBuilder resultadoAscii = new StringBuilder();
    for (char mensagemChar : mensagem) {
      resultadoAscii.append(mensagemChar+" = " + Integer.toString(mensagemChar)+"\n");
      asciiTextArea.setText(resultadoAscii.toString());
    }
  }//fim do metodo exibirAscii
  
  public void exibirBits (String mensagemDigitada) {
    StringBuilder resultadoBits = new StringBuilder();
    StringBuilder mensagemEmBits = new StringBuilder();
    char[] mensagem = mensagemDigitada.toCharArray();
    for (char mensagemChar : mensagem) {
      resultadoBits.append(mensagemChar+" = "+String.format("%8s", Integer.toBinaryString(mensagemChar)).replaceAll(" ", "0")+"\n");
      mensagemEmBits.append(String.format("%8s", Integer.toBinaryString(mensagemChar)).replaceAll(" ", "0"));
      bitsTextArea.setText(resultadoBits.toString());
    }
    setMensagemCodificada(mensagemEmBits.toString());
  }//Fim do exibirBits

  public void setMensagem(String mensagem) {
    this.mensagem = mensagem;
  }

  public String getMensagem() {
    return mensagem;
  }
  
  public void setCodificacao(int codificacao) {
    this.codificacao = codificacao;
  }

  public int getCodificacao() {
    return codificacao;
  }

  public void setMensagemCodificada(String mensagemCodificada) {
    this.mensagemCodificada = mensagemCodificada;
  }

  public String getMensagemCodificada() {
    return mensagemCodificada;
  }
  
}

import java.math.BigInteger;
import java.util.Vector;

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

  private String mensagem, menuItem;
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
    //CamadaDeAplicacaoTransmissora (mensagemDigitada);
    exibirAscii(codificacao);
    textTextArea.setText("");
  }

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

  public void CamadaDeAplicacaoTransmissora (String mensagem) {
    String ascii = mensagem;
    int tipoDeCodificacao = getCodificacao();
    int quadro[] = new int [mensagem.length()];
    for (int i = 0; i < mensagem.length(); i++) {
      quadro[i] = Integer.parseInt(mensagem);
    }

    CamadaFisicaTransmissora (quadro, tipoDeCodificacao);
  }//Fim do metodo CamadaDeAplicacaoTransmissora

  public void CamadaFisicaTransmissora (int quadro[], int tipoDeCodificacao) {
    int fluxoBrutoDeBits []; //ATENÇÃO: trabalhar com BITS!!!
    switch (tipoDeCodificacao) {
    case 0 : //codificao binaria
    fluxoBrutoDeBits = CamadaFisicaTransmissoraCodificacaoBinaria(quadro);
    break;
    case 1 : //codificacao manchester
    fluxoBrutoDeBits = CamadaFisicaTransmissoraCodificacaoManchester(quadro);
    break;
    case 2 : //codificacao manchester diferencial
    fluxoBrutoDeBits = CamadaFisicaTransmissoraCodificacaoManchesterDiferencial(quadro);
    break;
    }//fim do switch/case
    MeioDeComunicacao(fluxoBrutoDeBits);
  }//fim da classe CamadaFisicaTransmissora

  public int[] CamadaFisicaTransmissoraCodificacaoBinaria (int quadro[]){
    String mensagem = getMensagem();
    int codificacaoBinaria;
    for (int i = 0; i < quadro.length; i++) {
      codificacaoBinaria = quadro[i];
    }
    Integer.toBinaryString(codificacaoBinaria);
    return codificacaoBinaria;
  }//fim do metodo CamadaFisicaTransmissoraCodificacaoBinaria

  public int[] CamadaFisicaTransmissoraCodificacaoManchester (int quadro[]){
    
    return 0;
  }//fim do metodo CamadaFisicaTransmissoraCodificacaoManchester

  public int[] CamadaFisicaTransmissoraCodificacaoManchesterDiferencial (int quadro[]){
    
    return 0;
  }//fim do CamadaFisicaTransmissoraCodificacaoManchesterDiferencial

  public void MeioDeComunicacao (int fluxoBrutoDeBits[]){

  }

  public void exibirAscii(int codificacao){
    char[] mensagem = textTextArea.getText().toCharArray();
    int[] mensagemAscii = new int[mensagem.length];
    for(int i = 0;i < mensagem.length;i++){
      mensagemAscii[i] = mensagem[i];
      adicionarNaExibicaoAscii(mensagem[i], mensagemAscii[i]);
    } 
  }//fim do metodo exibirAscii
  
  public void adicionarNaExibicaoAscii(char caractere, int numero){
    asciiTextArea.setText(asciiTextArea.getText() + caractere + " = " + numero + "\n");
  }//fim do metodo adicionarNaExibicaoAscii

  public void exibirBits (int codificacao) {

  }
  public void adicionarNaExibicaoBits (char caractere, int bits){
    
  }
}

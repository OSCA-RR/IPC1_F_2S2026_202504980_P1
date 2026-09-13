package ipc1.refugio.utilidades;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

// Limita la cantidad maxima de caracteres que se pueden escribir en un JTextField.
// Se usa para que no se puedan pegar textos enormes en los campos del formulario.
public class TextoLimitado extends PlainDocument {

    private final int limite;   // maximo de caracteres permitidos

    public TextoLimitado(int limite) {
        this.limite = limite;
    }

    // Solo deja insertar texto si no se pasa del limite.
    @Override
    public void insertString(int offset, String texto, AttributeSet estilo)
            throws BadLocationException {

        if (texto == null) return;

        if (getLength() + texto.length() <= limite) {
            super.insertString(offset, texto, estilo);
        }
    }
}
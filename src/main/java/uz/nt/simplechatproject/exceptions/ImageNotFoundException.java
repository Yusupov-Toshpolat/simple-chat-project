package uz.nt.simplechatproject.exceptions;

public class ImageNotFoundException extends Throwable{
    public ImageNotFoundException(String content) {
        super(content);
    }
}

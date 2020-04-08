package krenc.mirek.thirdweek.sb2k.teaipracadomowa3.service;

public class ItemNotFoundException extends RuntimeException {

        private String message;

    public ItemNotFoundException(String message) {
        this.message = message;
    }
}

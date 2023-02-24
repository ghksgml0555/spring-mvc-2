package hello.upload.domain;

import lombok.Data;

@Data
public class UploadFile {

    //사용자가 업로드한 파일네임과 서버에서 저장하는 파일네임을 구분
    //구분하는이유 : 두명의 사용자가 같은 파일명으로 업로드했을때 덮어지지 않도록 안겹치게 만든다.
    private String uploadFileName;
    private String storeFileName;

    public UploadFile(String uploadFileName, String storeFileName){
        this.uploadFileName = uploadFileName;
        this.storeFileName = storeFileName;
    }
}

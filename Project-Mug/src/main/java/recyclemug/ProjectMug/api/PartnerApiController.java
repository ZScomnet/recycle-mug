package recyclemug.ProjectMug.api;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import recyclemug.ProjectMug.domain.user.Partner;
import recyclemug.ProjectMug.service.PartnerService;

import javax.validation.Valid;

@Controller
@RequiredArgsConstructor
public class PartnerApiController {

    private final PartnerService partnerService;

    @PostMapping("/join/partner")
    public CreatePartnerResponse savePartner(@RequestBody @Valid CreatePartnerRequest request) {
        Partner partner = Partner.createPartner(request.getEmail(), request.getPassword(), request.getPhoneNumber(),
                request.getRegistrationNumber(), request.getZipcode(), request.getDetailAddress());
        try {
            partnerService.join(partner);
        } catch (IllegalStateException e) {
            return new CreatePartnerResponse("fail", e.toString());
        }

        return new CreatePartnerResponse("success", "회원가입에 성공했습니다.");
    }


    @Data
    static class CreatePartnerRequest {
        private String email;
        private String password;
        private String phoneNumber;
        private String zipcode;
        private String detailAddress;
        private String registrationNumber;
    }

    @Data
    @AllArgsConstructor
    static class CreatePartnerResponse {
        private String result;
        private String message;
    }
}

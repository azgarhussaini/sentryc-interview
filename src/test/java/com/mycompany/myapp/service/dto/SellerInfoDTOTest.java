package com.mycompany.myapp.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class SellerInfoDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(SellerInfoDTO.class);
        SellerInfoDTO sellerInfoDTO1 = new SellerInfoDTO();
        sellerInfoDTO1.setId(UUID.randomUUID());
        SellerInfoDTO sellerInfoDTO2 = new SellerInfoDTO();
        assertThat(sellerInfoDTO1).isNotEqualTo(sellerInfoDTO2);
        sellerInfoDTO2.setId(sellerInfoDTO1.getId());
        assertThat(sellerInfoDTO1).isEqualTo(sellerInfoDTO2);
        sellerInfoDTO2.setId(UUID.randomUUID());
        assertThat(sellerInfoDTO1).isNotEqualTo(sellerInfoDTO2);
        sellerInfoDTO1.setId(null);
        assertThat(sellerInfoDTO1).isNotEqualTo(sellerInfoDTO2);
    }
}

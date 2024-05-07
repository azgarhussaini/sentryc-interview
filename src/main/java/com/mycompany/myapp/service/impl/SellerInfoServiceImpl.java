package com.mycompany.myapp.service.impl;

import com.mycompany.myapp.domain.SellerInfo;
import com.mycompany.myapp.repository.SellerInfoRepository;
import com.mycompany.myapp.service.SellerInfoService;
import com.mycompany.myapp.service.dto.SellerInfoDTO;
import com.mycompany.myapp.service.mapper.SellerInfoMapper;
import java.util.Optional;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.mycompany.myapp.domain.SellerInfo}.
 */
@Service
@Transactional
public class SellerInfoServiceImpl implements SellerInfoService {

    private final Logger log = LoggerFactory.getLogger(SellerInfoServiceImpl.class);

    private final SellerInfoRepository sellerInfoRepository;

    private final SellerInfoMapper sellerInfoMapper;

    public SellerInfoServiceImpl(SellerInfoRepository sellerInfoRepository, SellerInfoMapper sellerInfoMapper) {
        this.sellerInfoRepository = sellerInfoRepository;
        this.sellerInfoMapper = sellerInfoMapper;
    }

    @Override
    public SellerInfoDTO save(SellerInfoDTO sellerInfoDTO) {
        log.debug("Request to save SellerInfo : {}", sellerInfoDTO);
        SellerInfo sellerInfo = sellerInfoMapper.toEntity(sellerInfoDTO);
        sellerInfo = sellerInfoRepository.save(sellerInfo);
        return sellerInfoMapper.toDto(sellerInfo);
    }

    @Override
    public SellerInfoDTO update(SellerInfoDTO sellerInfoDTO) {
        log.debug("Request to update SellerInfo : {}", sellerInfoDTO);
        SellerInfo sellerInfo = sellerInfoMapper.toEntity(sellerInfoDTO);
        sellerInfo = sellerInfoRepository.save(sellerInfo);
        return sellerInfoMapper.toDto(sellerInfo);
    }

    @Override
    public Optional<SellerInfoDTO> partialUpdate(SellerInfoDTO sellerInfoDTO) {
        log.debug("Request to partially update SellerInfo : {}", sellerInfoDTO);

        return sellerInfoRepository
            .findById(sellerInfoDTO.getId())
            .map(existingSellerInfo -> {
                sellerInfoMapper.partialUpdate(existingSellerInfo, sellerInfoDTO);

                return existingSellerInfo;
            })
            .map(sellerInfoRepository::save)
            .map(sellerInfoMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<SellerInfoDTO> findOne(UUID id) {
        log.debug("Request to get SellerInfo : {}", id);
        return sellerInfoRepository.findById(id).map(sellerInfoMapper::toDto);
    }

    @Override
    public void delete(UUID id) {
        log.debug("Request to delete SellerInfo : {}", id);
        sellerInfoRepository.deleteById(id);
    }
}

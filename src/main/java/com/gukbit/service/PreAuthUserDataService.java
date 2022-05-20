package com.gukbit.service;

import com.gukbit.domain.PreAuthUserData;
import com.gukbit.repository.PreAuthUserDataRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PreAuthUserDataService {
    private final PreAuthUserDataRepository preAuthUserDataRepository;
    private final ImageService imageService;

    public void deletePreAuthUserData(Integer authId){
        preAuthUserDataRepository.deleteById(authId);
        PreAuthUserData preAuthUserData = this.getPreAuthUserData(authId);
        imageService.deleteFile(preAuthUserData.getSaveFileName());
    }

    public PreAuthUserData getPreAuthUserData(Integer authId){
        return preAuthUserDataRepository.getById(authId);
    }

    public List<PreAuthUserData> getPreAuthUserDataListByUserId(String userId){return preAuthUserDataRepository.findAllByUserIdContaining(userId);}
}

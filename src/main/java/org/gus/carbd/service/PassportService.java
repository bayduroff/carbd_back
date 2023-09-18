package org.gus.carbd.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PassportService {

/*    private final PassportDtoMapper passportDtoMapper;

    private  final PassportRepository passportRepository;

    public Passport getPassportByPassportId(int passportId) {
        Optional<Passport> result = passportRepository.findById(passportId);
        Passport passport;
        if (result.isPresent()) {
            passport = result.get();
        } else {
            throw new ResourceNotFoundException("Did not find passport passportId - " + passportId);
        }

        return passport;
    }

    @Transactional
    public void editPassportByPassportId(int passportId, PassportDto changedPassportDto) {
        passportDtoMapper.updatePassport(getPassportByPassportId(passportId), changedPassportDto);
    }*/
}

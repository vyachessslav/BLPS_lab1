package gmail.vezhur2003.blps.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import gmail.vezhur2003.blps.DTO.VacancyData;
import gmail.vezhur2003.blps.entity.VacancyEntity;
import gmail.vezhur2003.blps.repository.VacancyRepository;
import gmail.vezhur2003.blps.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;

@Service
public class VacancyService {

    @Autowired
    private VacancyRepository vacancyRepository;

    @Autowired
    private UserRepository userRepository;

    public VacancyData createVacancy(VacancyData vacancy) {

        if (!userRepository.getById(vacancy.getUserId()).getRole().equals("employer")) {
            throw new IllegalArgumentException("User must be employer");
        }
        if (vacancy.getName() == null || vacancy.getName().isEmpty()) {
            throw new IllegalArgumentException("Vacancy name cannot be empty");
        }
        if (vacancy.getName().length() > 255) {
            throw new IllegalArgumentException("Vacancy name cannot be longer than 255 symbols");
        }

        VacancyEntity vacancyEntity = new VacancyEntity();
        vacancyEntity.setName(vacancy.getName());
        vacancyEntity.setSalary(vacancy.getSalary());
        vacancyEntity.setCompany(vacancy.getCompany());
        vacancyEntity.setLocation(vacancy.getLocation());
        vacancyEntity.setContact(vacancy.getContact());
        vacancyEntity.setShortDescription(vacancy.getShortDescription());
        vacancyEntity.setLongDescription(vacancy.getLongDescription());
        vacancyEntity.setUserId(vacancy.getUserId());

        return new VacancyData(vacancyRepository.save(vacancyEntity));
    }

    public VacancyData getVacancyById(Long vacancyId) {
        try {
            VacancyEntity vacancy = vacancyRepository.findById(vacancyId).orElse(null);
            if (vacancy == null) {
                return new VacancyData();
            }
            return new VacancyData(vacancy);
        } catch (Exception e) {
            throw new IllegalArgumentException("Internal error while finding vacancy");
        }
    }

    public void deleteVacancyById(Long vacancyId, Long userId) {
        try {
            VacancyEntity vacancy = vacancyRepository.findById(vacancyId).orElse(null);
            if (vacancy == null) {
                throw new IllegalArgumentException("There is no vacancy with this id");
            }
            if (userId != vacancy.getUserId()) {
                throw new IllegalArgumentException("Vacancy does not belong to this user");
            }
            vacancyRepository.deleteById(vacancyId);
        } catch (Exception e) {
            throw new IllegalArgumentException("Internal error while deleting vacancy");
        }
    }

    public List<VacancyData> searchVacancies() {
        List<VacancyEntity> vacancyEntities = vacancyRepository.findAll();
        List<VacancyData> vacancyDataList = new ArrayList<>();
        for (VacancyEntity ie : vacancyEntities) {
            vacancyDataList.add(new VacancyData(ie));
        }
        return vacancyDataList;
    }
    
}
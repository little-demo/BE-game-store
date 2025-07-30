package antran.project.Service;

import antran.project.DTO.Request.CardEffectRequest;
import antran.project.DTO.Response.CardEffectResponse;
import antran.project.Entity.CardEffect;
import antran.project.Mapper.CardEffectMapper;
import antran.project.Repository.CardEffectRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class CardEffectService {
    CardEffectRepository cardEffectRepository;
    CardEffectMapper cardEffectMapper;

    public CardEffectResponse createEffect(CardEffectRequest request) {
        CardEffect effect = cardEffectMapper.toCardEffect(request);

        cardEffectRepository.save(effect);
        return cardEffectMapper.toCardEffectResponse(effect);
    }

    public List<CardEffectResponse> getAllEffects() {
        return cardEffectRepository.findAll()
                .stream()
                .map(cardEffectMapper::toCardEffectResponse)
                .collect(Collectors.toList());
    }

    public CardEffectResponse getEffectById(Long id) {
        CardEffect effect = cardEffectRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("CardEffect not found with id: " + id));
        return cardEffectMapper.toCardEffectResponse(effect);
    }

    public CardEffectResponse updateEffect(Long id, CardEffectRequest request) {
        CardEffect effect = cardEffectRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("CardEffect not found with id: " + id));

        effect.setName(request.getName());
        effect.setValue(request.getValue());
        effect.setType(request.getType());
        effect.setTarget(request.getTarget());

        cardEffectRepository.save(effect);
        return cardEffectMapper.toCardEffectResponse(effect);
    }

    public void deleteEffect(Long id) {
        if (!cardEffectRepository.existsById(id)) {
            throw new RuntimeException("CardEffect not found with id: " + id);
        }
        cardEffectRepository.deleteById(id);
    }
}

package com.ilya.orderservice.services;

import com.ilya.orderservice.dtos.BagDto;
import com.ilya.orderservice.models.Bag;
import com.ilya.orderservice.repositories.BagsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BagsService {

    private final BagsRepository bagsRepository;
    private final FoodsService foodsService;

    public List<BagDto> findAll(Long userId){
        return bagsRepository.findAllByUserId(userId)
                .stream().map(this::mapToBagDto).collect(Collectors.toList());
    }

    @Transactional
    public void save(BagDto bagDto){
        bagsRepository.save(mapToBag(bagDto));
    }

    @Transactional
    public void delete(Long id){
        bagsRepository.deleteById(id);
    }

    @Transactional
    public void update(Long id, Long quantity){
        Optional<Bag> optBag = bagsRepository.findById(id);
        Bag bag = optBag.get();
        bag.setQuantity(quantity);
        bagsRepository.save(bag);
    }

    @Transactional
    public void clearCart(Long userId){
        bagsRepository.deleteAllByUserId(userId);
    }

    private BagDto mapToBagDto(Bag bag){
        return new BagDto(bag.getId(), bag.getUserId(), bag.getFood().getId(), bag.getFood().getName(),
                bag.getFood().getPrice(), bag.getQuantity());
    }

    private Bag mapToBag(BagDto bagDto){
        return new Bag(bagDto.getId(), bagDto.getUserId(),
                foodsService.findFoodById(bagDto.getFoodId()), bagDto.getQuantity());
    }
}

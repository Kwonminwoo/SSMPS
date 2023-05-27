package capstone_design_1.ssmps_backend.controller;

import capstone_design_1.ssmps_backend.domain.Location;
import capstone_design_1.ssmps_backend.domain.Store;
import capstone_design_1.ssmps_backend.dto.AddLocationRequest;
import capstone_design_1.ssmps_backend.dto.LocationRequest;
import capstone_design_1.ssmps_backend.dto.LocationResponse;
import capstone_design_1.ssmps_backend.dto.RequestLocation;
import capstone_design_1.ssmps_backend.dto.store.StoreRequest;
import capstone_design_1.ssmps_backend.dto.store.StoreResponse;
import capstone_design_1.ssmps_backend.service.StoreService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@RestController
public class StoreController {
    private final StoreService storeService;

    @GetMapping("/api/location")
    public ResponseEntity<Object> getLocationList(@RequestParam Long storeId){
        List<Location> locationList = storeService.getLocationList(storeId);
//        locationList.stream()
//                .map(l -> new LocationResponse(l.getId(), l.getStartX(), l.getStartY(), l.getEndX(), l.getEndY(), l.getItemList())))
        return ResponseEntity.ok(null);
    }

//    @PostMapping("/api/location")
//    public ResponseEntity<Object> addLocation(@RequestBody AddLocationRequest locationRequest){
////        storeService.addLocation();
//        StoreRequest store = locationRequest.getStore();
////        Store nowStore = new Store(store.getId(), store.getName(), store.getAddress(), store.)
//        List<LocationRequest> locationList = locationRequest.getLocationList();
//
////        locationList.stream()
////                        .map(l -> new Location(null, ))
////        storeService.addLocation();
//        return ResponseEntity.ok(null);
//    }


    // 매니저 스토어 찾기
    @GetMapping("api/storeList/{id}")
    public ResponseEntity<Object> findAllStore(@PathVariable Long id){
        List<Store> allStore = storeService.findAllStoreById(id);
        List<StoreResponse> resultList = allStore.stream()
                .map(s -> new StoreResponse(s.getId(), s.getName(), s.getAddress(), s.getLocationList()))
                .collect(Collectors.toList());
        return ResponseEntity.ok(resultList);
    }

//     로케이션 찾기
    @GetMapping("api/store/location/{id}")
    public ResponseEntity<Object> findStoreLocation(@PathVariable(name = "id") Long storeId){
        Store store = storeService.findStoreById(storeId);
        return ResponseEntity.ok(store.getLocationList());
    }

    @PostMapping("api/store/location")
    public ResponseEntity<Object> registStoreLocation(@RequestBody StoreRequest store){

        log.error("location size", store.getLocationList().size() + "");
        List<Location> locationList = store.getLocationList().stream()
                .map(l -> new Location(l.getId(), l.getStartX(), l.getStartY(),
                        l.getEndX(), l.getEndY(), null))
                .collect(Collectors.toList());

        List<Location> savedLocations = storeService.registStoreLocation(store.getId(), locationList);
        List<Location> resultList = savedLocations.stream()
                .map(l -> new Location(l.getId(), l.getStartX(), l.getStartY(), l.getEndX(), l.getEndY(), null))
                .collect(Collectors.toList());
//        Store updatedStore = storeService.updateStore(findStore);
        return ResponseEntity.ok(resultList);
    }



}
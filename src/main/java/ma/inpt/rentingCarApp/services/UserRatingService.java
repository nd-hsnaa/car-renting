package ma.inpt.rentingCarApp.services;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import com.mysql.cj.x.protobuf.MysqlxDatatypes.Array;

import ma.inpt.rentingCarApp.DAO.CarRepository;
import ma.inpt.rentingCarApp.DAO.UserRatingRepository;
import ma.inpt.rentingCarApp.DAO.UserRepository;
import ma.inpt.rentingCarApp.entities.Car;
import ma.inpt.rentingCarApp.entities.User;
import ma.inpt.rentingCarApp.entities.UserRating;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;


@Service
public class UserRatingService {

    // class attributes :
    final CarRepository carRepository;
    final UserRepository usRepo;
    final UserRatingRepository ratingRepo;
    final UserService userService;
    // final UserRating rating;
    final CarService carService;

    // class constructor :
    public UserRatingService( CarRepository carRepository, UserRepository usRepo, UserRatingRepository ratingRepo, @Lazy CarService carService, @Lazy UserService userService) {
        this.carRepository = carRepository;
        this.usRepo = usRepo;
        this.ratingRepo = ratingRepo;
        // this.rating = rating;
        this.carService = carService;
        this.userService = userService;
    }

    // class methods :
    public void findByUser() {
        
    }

    public void addReturnedCar(Car car) {
        long carId = car.getCarId();
        long userId = car.getTheUser().getUserId();
        UserRating userRating = new UserRating(carId, userId, 0, null, false);
        ratingRepo.save(userRating);
    }

    public List<Car> getReturnedCar(User user) {

        List<Car> returnedCars = new ArrayList<>();
        List<UserRating> userRatings = new ArrayList<>();
        for (UserRating userRating : ratingRepo.findAll()) {
            if (userRating.getUserId() == user.getUserId()){
                if (userRating.getDone() == false) {
                    userRatings.add(userRating);
                    Car car = carService.findById(userRating.getCarId());
                    returnedCars.add(car);
                }
            }
        }

        return returnedCars;
    }

    public List<UserRating> getReviewByCarId(Long carId) {

        List<UserRating> userRatings = new ArrayList<>();
        for (UserRating userRating : ratingRepo.findAll()) {
            if (userRating.getCarId() == carId){
                if (userRating.getDone() == true) {
                    userRatings.add(userRating);
                }
            }
        }

        return userRatings;
    }

    public List<List<String>> getReviewCarList(List<UserRating> ratings) {

        List<String> reviewsInfo = null;
        List<List<String>> allReviewInfo = new ArrayList<>();

        // Map<String, String> reviewList = new LinkedHashMap<>();
        // Map<String, String> listedCarReservations = new LinkedHashMap<>();

        for (UserRating userRating : ratings) {
            reviewsInfo = new ArrayList<String>();
            User user = (User) userService.findById(userRating.getUserId());
            String reviewerName = user.getFirstName() + " " + user.getLastName();
            reviewsInfo.add(reviewerName);
            reviewsInfo.add(userRating.getComment());
            reviewsInfo.add(Integer.toString(userRating.getRatingValue()));        
            allReviewInfo.add(reviewsInfo); 
        }

        return allReviewInfo;
    }

    public void updateRating(Long userId, Long carId, int ratingValue, String comment, boolean done) {
        for (UserRating userRating : ratingRepo.findAll()) {
            if (userRating.getUserId() == userId) {
                if (userRating.getCarId() == carId) {
                    userRating.setRatingValue(ratingValue);
                    userRating.setComment(comment);
                    userRating.setDone(done);
                    ratingRepo.save(userRating);
                    getAverageRating(carService.findById(carId));
                }
            }
        }
    }

    public void getAverageRating(Car car) {
        List<Integer> rating = new ArrayList<>();
        for (UserRating userRating : ratingRepo.findAll()) {
            if (userRating.getCarId() == car.getCarId()) {
                rating.add(userRating.getRatingValue());
            }
        }
        car.setRating(getMaxRatingFrequency(rating));
        car.setTotalReview(rating.size());
        carRepository.save(car);
    }

    public int getMaxRatingFrequency(List<Integer> ratings) {
        Map<Integer, Integer> ratingHash = new HashMap<Integer, Integer>();
        for (int rating : ratings) {
            int key = rating;
            if (ratingHash.containsKey(key)) {
                int frequency = ratingHash.get(key);
                frequency++;
                ratingHash.put(key, frequency);
            } else {
                ratingHash.put(key, 1);
            }
        }

        int maxRating = 0; 
        int res = -1;

        for (Entry<Integer, Integer> val : ratingHash.entrySet()) {
            if (maxRating < val.getValue()) {
                res = val.getKey();
                maxRating = val.getValue();
            }
        }

        return res;
    }
}


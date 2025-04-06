package m1.info.reza.planning;

import m1.info.reza.response.ApiResponse;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/restaurant/:id/planning")
public class RestaurantPlanningController {

    @PostMapping("/create")
    public void create(){
    }

}

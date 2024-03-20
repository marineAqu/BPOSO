package teamcom.comfirstpro.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import teamcom.comfirstpro.service.WantseeService;

@Controller
@RequiredArgsConstructor
public class WantseeController {
    private final WantseeService wantseeService;

    @PostMapping("saveWantsee")
    public @ResponseBody void saveWantsee(@RequestParam("userId") String userId,
                                          @RequestParam("heartChk") int heartChk, @RequestParam("movName") String movName) {
        System.out.println("saveVocaTit 함수 들어옴 (컨트롤러)");
        wantseeService.saveWantsee(userId, heartChk, movName);
    }
}

package springFinal.POS.web;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import springFinal.POS.domain.Item.Item;
import springFinal.POS.domain.Item.service.ItemService;
import springFinal.POS.domain.Pos.MyPos;
import springFinal.POS.domain.Pos.service.MyPosService;
import springFinal.POS.domain.User.User;
import springFinal.POS.domain.User.service.UserService;
import springFinal.POS.web.dto.MessageDTO;
import springFinal.POS.web.dto.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static java.util.stream.Collectors.toList;
import static springFinal.POS.domain.Item.repository.ItemRepository.*;

@Controller
@RequiredArgsConstructor
public class MyPosController {
    private final UserService userService;
    private final ItemService itemService;
    private final MyPosService myPosService;
    public static final String LOGIN_USER = "loginUser";

    @GetMapping("/")
    public String notLoginHome(@SessionAttribute(name = LOGIN_USER, required = false) User loginUser) {
        if (loginUser == null) {
            return "notLoginHome";
        } else return "redirect:/loginHome";
    }

    @GetMapping("/join")
    public String joinForm() {
        return "join";
    }

    @PostMapping("/join")
    public String join(@ModelAttribute JoinDto joinDto) {
        userService.join(User.createUser(joinDto));
        return "redirect:/";
    }

    @PostMapping("/login")
    public String login(@ModelAttribute LoginDto loginDto,
                        HttpServletRequest request) {
        User loginUser = userService.login(loginDto);

        if (loginUser == null) {
            return "home";
        }

        HttpSession session = request.getSession();

        session.setAttribute(LOGIN_USER, loginUser);

        return "redirect:/";
    }

    @GetMapping("loginHome")
    public String loginHome(Model model) {
        MyPos myPos = myPosService.getPos();

        List<TurnoverDto> dayRecord = dayTurnoverDtos(myPos);
        List<TurnoverDto> weekRecord = weekTurnoverDtos(myPos);

        model.addAttribute("itemList", itemService.findAll());
        model.addAttribute("balance", myPos.getBalance());
        model.addAttribute("monthRecord", myPos.getMonthTurnover());
        model.addAttribute("dayRecordList", dayRecord);
        model.addAttribute("weekRecordList", weekRecord);
        return "loginHome";
    }
    @GetMapping("/addItem")
    public String addItemForm() {
        return "itemAddForm";
    }

    @PostMapping("/addItem")
    public String addItem(@ModelAttribute ItemAddDto dto) {
        Item item = itemService.findByName(dto.getName());

        if (item == null) itemService.save(Item.createItem(dto));
        else itemService.recover(item);

        return "itemAddForm";
    }
    @GetMapping("/dropItem")
    public String dropItemForm(Model model) {
        List<ItemMapping> allName = itemService.findAllName();

        model.addAttribute("itemNames", allName);
        return "itemDropForm";
    }
    @PostMapping("/dropItem")
    public String dropItem(@ModelAttribute DropItemDto items) {
        items.getDropped().forEach(name-> {
            itemService.deleteItem(name);
        });
        return "redirect:/dropItem";
    }
    @PostMapping("/payment")
    @ResponseBody
    public MessageDTO payment(Model model, @RequestBody SaleData saleData) {
        itemService.itemSale(saleData.getItemDataList());
        myPosService.updateTurnover(saleData.getSummary());
        return new MessageDTO("결제 완료되었습니다!");
    }
    @GetMapping("/record/{dayOrMonth}")
    public String payment(Model model, @PathVariable String dayOrMonth) {
        List<Item> all = itemService.findAll();
        List<SaleDetailDto> saleDetailDtoList = new ArrayList<>();
        MyPos pos = myPosService.getPos();

        model.addAttribute("date", dayOrMonth);

        if(dayOrMonth.length()==7) {
            model.addAttribute("dayOrMonth", "month");
            model.addAttribute("turnover", pos.getMonthTurnover().get(dayOrMonth));
            all.forEach(item -> {
                if (item.getMonthSales().containsKey(dayOrMonth)) {
                    saleDetailDtoList.add(new SaleDetailDto(item.getName(), item.getMonthSales().get(dayOrMonth), item.getPrice()* item.getMonthSales().get(dayOrMonth)));
                }
            });
        }

        else if (dayOrMonth.length()==23) {
            model.addAttribute("dayOrMonth", "week");
            model.addAttribute("turnover", pos.getWeekTurnover().get(dayOrMonth));
            all.forEach(item -> {
                if (item.getWeekSales().containsKey(dayOrMonth)) {
                    saleDetailDtoList.add(new SaleDetailDto(item.getName(), item.getWeekSales().get(dayOrMonth), item.getPrice()*item.getWeekSales().get(dayOrMonth)));
                }
            });
        }

        else if (dayOrMonth.length()==10) {
            model.addAttribute("dayOrMonth", "day");
            model.addAttribute("turnover", pos.getDayTurnover().get(dayOrMonth));
            all.forEach(item -> {
                if (item.getDaySales().containsKey(dayOrMonth)) {
                    saleDetailDtoList.add(new SaleDetailDto(item.getName(), item.getDaySales().get(dayOrMonth), item.getPrice()*item.getDaySales().get(dayOrMonth)));
                }
            });
        }

        int max=0;
        String bestItem = "";

        for (SaleDetailDto detail : saleDetailDtoList) {
            if (detail.getNumber() >= max) {
                max = detail.getNumber();
                bestItem = detail.getName();
            }
        }
        model.addAttribute("bestItem", bestItem);
        model.addAttribute("saleDetailList", saleDetailDtoList);
        return "saleDetailPage";
    }
    @GetMapping("/stockCheck")
    public String stockManage(Model model) {
        model.addAttribute("stockDays", myPosService.getPos().getStockDay());
        return "stockCheck";
    }
    @GetMapping("/stockDetail/{stockDay}")
    public String stockManage(Model model, @PathVariable String stockDay) {
        List<Item> all = itemService.findAll();

        List<StockDetailDto> collect = itemService.findAll().stream()
                .map(item -> new StockDetailDto(item.getExist(), item.getName(), item.getDateStock().get(stockDay)))
                .collect(toList());

        model.addAttribute("stockDetails", collect);
        model.addAttribute("stockDay", stockDay);
        return "stockDetail";
    }

    @GetMapping("/addStock")
    public String addStockForm(Model model) {
        model.addAttribute("items",itemService.findAll());
        return "addStockForm";
    }

    @PostMapping("/addStock")
    public String addStock(@Valid @ModelAttribute AddStockDto numbers) {

        List<Integer> number = numbers.getItemNumber();
        List<String> name = numbers.getItemName();


        DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String date = LocalDateTime.now().format(format);

        myPosService.addStockDay(date);

        for (int i = 0; i < number.size(); i++) {
            itemService.addStock(name.get(i), number.get(i), date);
        }

        return "redirect:/addStock";
    }

    @EventListener(ApplicationReadyEvent.class)
    public void initData() {
        myPosService.startPos();
    }

    //== 비즈니스 메서드 ==//
    /**
     * JPA에서 가져온 map은 순서를 보장하지 않기 때문에 직접 정렬 후 DTO에 담기 / day 버전
     * @param myPos
     * @return
     */
    public static List<TurnoverDto> dayTurnoverDtos(MyPos myPos) {
        Map<String, Integer> dayTurnover = myPos.getDayTurnover();

        List<TurnoverDto> dayRecord = new ArrayList<>();

        new ArrayList<>(dayTurnover.keySet())
                .stream().sorted().toList().forEach(date -> {
            dayRecord.add(new TurnoverDto(date, dayTurnover.get(date)));
        });
        return dayRecord;
    }
    /**
     * JPA에서 가져온 map은 순서를 보장하지 않기 때문에 직접 정렬 후 DTO에 담기 / week 버전
     * @param myPos
     * @return
     */
    public static List<TurnoverDto> weekTurnoverDtos(MyPos myPos) {
        Map<String, Integer> dayTurnover = myPos.getWeekTurnover();

        List<TurnoverDto> dayRecord = new ArrayList<>();

        new ArrayList<>(dayTurnover.keySet())
                .stream().sorted().toList().forEach(date -> {
                    dayRecord.add(new TurnoverDto(date, dayTurnover.get(date)));
                });
        return dayRecord;
    }

    /**
     * JPA에서 가져온 map은 순서를 보장하지 않기 때문에 직접 정렬 후 DTO에 담기 / month 버전
     * @param myPos
     * @return
     */
    public static List<TurnoverDto> monthTurnoverDtos(MyPos myPos) {
        Map<String, Integer> dayTurnover = myPos.getMonthTurnover();

        List<TurnoverDto> dayRecord = new ArrayList<>();

        new ArrayList<>(dayTurnover.keySet())
                .stream().sorted().toList().forEach(date -> {
                    dayRecord.add(new TurnoverDto(date, dayTurnover.get(date)));
                });
        return dayRecord;
    }
}
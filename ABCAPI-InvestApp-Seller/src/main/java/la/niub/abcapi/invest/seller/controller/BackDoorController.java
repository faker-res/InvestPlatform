package la.niub.abcapi.invest.seller.controller;

import la.niub.abcapi.invest.seller.dao.invest.TraderMarkTaskMapper;
import la.niub.abcapi.invest.seller.model.TraderMarkTaskModel;
import la.niub.abcapi.invest.seller.model.response.Response;
import la.niub.abcapi.invest.seller.service.IBackDoorService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author : ppan
 * @description : 线上环境修改数据库、执行测试接口
 * @date : 2019-02-25 10:19
 */
@RestController
@RequestMapping("/backdoor")
public class BackDoorController {

    private Logger logger = LogManager.getLogger(getClass());

    @Autowired
    private IBackDoorService backDoorService;

    @Autowired
    private TraderMarkTaskMapper traderMarkTaskMapper;

    /**
     * 测试券商评分定时任务
     * @author zhairp createDate: 2019-02-14
     * @return
     */
    @GetMapping("/tradermark/job")
    public Response testTraderMarkJob(@RequestParam(defaultValue = "false") Boolean test) {
        try {
            backDoorService.testTraderMarkJob(test);
            return new Response();
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("测试券商评分任务定时生成失败：{}", e);
            return new Response(500, "测试券商评分任务定时生成失败：" + e);
        }
    }

    /**
     * 修改investnew_trader_mark_task表
     * @param taskModel
     * @return
     * @throws Exception
     */
    @PostMapping("/tradermark/task/update")
    public Response updateTraderMarkTask(@RequestBody TraderMarkTaskModel taskModel) throws Exception {
        if (taskModel.getId() == null) {
            return new Response("id必填");
        }
        return new Response(traderMarkTaskMapper.updateByPrimaryKeySelective(taskModel));
    }
}

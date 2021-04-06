package edu.hufe;

import edu.hufe.entity.DataSource;
import edu.hufe.entity.MusicInfo;
import edu.hufe.entity.PlayList;
import edu.hufe.mapper.PlayListMapper;
import edu.hufe.service.DataSourceService;
import edu.hufe.service.PlayListService;
import edu.hufe.service.PlayerService;
import edu.hufe.utils.Const;
import edu.hufe.utils.RedisUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import java.text.SimpleDateFormat;
import java.util.List;

@SpringBootTest
class FreeMusicApplicationTests {

    @Autowired
    private DataSourceService dataSourceService;

    @Autowired
    private PlayListService playListService;

    @Autowired
    private PlayListMapper playListMapper;

    @Autowired
    private PlayerService playerService;

    @Autowired
    private RedisUtil redisUtil;

    @Value("${spring.redis.open: #{false}}")
    private boolean open;

    @Test
    public void selectAllDataSource(){
        List<DataSource> sources = dataSourceService.findDataSourceList();
        for(DataSource source: sources){
            System.out.println(source);
        }
    }

    @Test
    public void selectAllPlayList(){
        List<PlayList> list = playListService.queryAllPlayList();
        for(PlayList item : list){
            System.out.println(item);
        }
    }

    @Test
    public void searchMusicTest(){
        List<MusicInfo> musicInfos = playerService.searchMusic("1","30","1","绿色");
        for(MusicInfo item : musicInfos){
            System.out.println(item);
        }
    }

    @Test
    public void redisSetTest(){
        System.out.println(open);
        List<PlayList> list = playListMapper.findAllPlayList();
//        for(PlayList item : list){
//            System.out.println(item);
//        }
        // System.out.println(redisUtil.toJson(list));
        redisUtil.set("playlist",list);

    }

    @Test
    public void redisGetTest(){
        List<PlayList> list = redisUtil.get("playlist",List.class);
        for(PlayList item : list){
            System.out.println(item);
        }
    }

    @Test
    public void timeTest(){
        /**
         * 如何存储每日榜单？   歌单id + 日期
         */
        // 获取当前日期
        System.out.println(new SimpleDateFormat(Const.DATE_FORMAT).format(System.currentTimeMillis()));
    }

}
package edu.hufe;

import edu.hufe.entity.DataSource;
import edu.hufe.entity.MusicInfo;
import edu.hufe.entity.PlayList;
import edu.hufe.service.DataSourceService;
import edu.hufe.service.PlayListService;
import edu.hufe.service.PlayerService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
class FreeMusicApplicationTests {

    @Autowired
    private DataSourceService dataSourceService;

    @Autowired
    private PlayListService playListService;

    @Autowired
    private PlayerService playerService;

    @Value("${spring.redis.host}")
    private String url;

    @Value("${spring.redis.port}")
    private int port;

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
    public void test(){
        System.out.println(url + "---->" + port);
    }

}
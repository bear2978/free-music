/**************************************************
 * Ajax 后台数据交互请求模块
 * 时间：2021-1-11
 *************************************************/

// ajax加载搜索结果
function ajaxSearch() {
    if(rem.wd === "") {
        layer.msg('搜索内容不能为空', {anim:6});
        return false;
    }
    
    if(rem.loadPage === 1) { // 弹出搜索提示
        var tmpLoading = layer.msg('搜索中', {icon: 16,shade: 0.01});
    }
    
    $.ajax({
        type: Player.method,
        url: "/search",
        data: "count=" + Player.loadCount + "&source=" + rem.sourceId + "&page=" + rem.loadPage + "&keyword=" + rem.wd,
        dataType: "json",
        complete: function(XMLHttpRequest, textStatus) {
            if(tmpLoading) layer.close(tmpLoading);    // 关闭加载中动画
        },
        success: function(jsonData){
            // 调试信息输出
            if(Player.debug) {
                console.debug("搜索结果数：" + jsonData.length);
            }
            // 加载第一页，清空列表
            if(rem.loadPage === 1) {
                // 返回结果为零
                if(jsonData.length === 0) {
                    layer.msg('没有找到相关歌曲', {anim:6});
                    return false;
                }
                musicList[0].item = [];
                rem.mainList.html('');   // 清空列表中原有的元素
                addListHead();     // 加载列表头
            } else {
                $("#list-foot").remove(); //已经是加载后面的页码了，删除之前的"加载更多"提示
            }
            
            if(jsonData.length === 0) {
                addListBar("nomore"); // 加载完了
                return false;
            }
            
            var tempItem = [], no = musicList[0].item.length;
            
            for (var i = 0; i < jsonData.length; i++) {
                // console.log(jsonData[i]);
                no ++;
                tempItem =  {
                    id: jsonData[i].id,            // 音乐ID
                    name: jsonData[i].name,        // 音乐名字
                    artist: jsonData[i].artist,    // 艺术家名字
                    album: jsonData[i].album,      // 专辑名字
                    source: jsonData[i].source,    // 音乐来源
                    picId: jsonData[i].picId,      // 封面ID
                    picUrl: jsonData[i].picUrl,    // 专辑图片链接
                    musicUrl: jsonData[i].musicUrl,// mp3链接
                    lyricId: jsonData[i].lyricId   // 歌词ID
                };
                musicList[0].item.push(tempItem);   // 保存到搜索结果临时列表中
                addItem(no, tempItem.name, tempItem.artist, tempItem.album);  // 在前端显示
            }
            
            rem.dislist = 0;    // 当前显示的是搜索列表
            rem.loadPage ++;    // 已加载的页数+1
            
            dataBox("list");    // 在主界面显示出播放列表
            refreshList();  // 刷新列表，添加正在播放样式
            
            if(no < Player.loadCount) {
                addListBar("nomore");  // 没加载满，说明已经加载完了
            } else {
                addListBar("more");     // 还可以点击加载更多
            }
            
            if(rem.loadPage === 2) listToTop();    // 播放列表滚动到顶部
        },   //success
        error: function(XMLHttpRequest, textStatus, errorThrown) {
            layer.msg('搜索结果获取失败 - ' + XMLHttpRequest.status);
            console.error(XMLHttpRequest + textStatus + errorThrown);
        }   // error
    });//ajax
}

// 获取系统所有数据源
function ajaxDataSource(callback) {
    $.ajax({
        type: Player.method,
        url: "getDataSource",
        dataType: "json",
        success: function (data) {
            // 调用回调函数
            if(callback) {
                callback(data);
            }
        }, //success
        error: function(XMLHttpRequest, textStatus, errorThrown) {
            layer.msg('获取系统数据源失败 - ' + XMLHttpRequest.status);
            console.error(XMLHttpRequest + textStatus + errorThrown);
        }   // error
    }); //ajax
}

// 获取播放列表系统内置歌单列表
function ajaxPlayList(callback) {
    $.ajax({
        type: Player.method,
        url: "playlist",
        dataType: "json",
        success: function (data) {
            for (let i = 0; i < data.length; i++) {
                musicList.push(data[i]);
            }
            // 调用回调函数
            if(callback) {
                callback();
            }
        }, //success
        error: function(XMLHttpRequest, textStatus, errorThrown) {
            layer.msg('获取系统歌单失败 - ' + XMLHttpRequest.status);
            console.error(XMLHttpRequest + textStatus + errorThrown);
        }   // error
    }); //ajax
}

// ajax加载歌单
// 参数：歌单网易云 id, 歌单存储 id,回调函数
function ajaxPlayListById(lid, sourceId, id, callback) {
    if(!lid){
        return false;
    }

    // 已经在加载了,跳过
    if(musicList[id].isloading === true) {
        return true;
    }

    musicList[id].isloading = true; // 更新状态：列表加载中

    $.ajax({
        type: Player.method,
        url: "detailPlaylist",
        data: "id=" + lid + "&source=" + sourceId,
        dataType : "json",
        complete: function(XMLHttpRequest, textStatus) {
            musicList[id].isloading = false; // 列表已经加载完了
        },  // complete
        success: function(data){
            for (let i = 0; i < data.length; i++){
                musicList[id].item.push(data[i]);
            }
            // 首页显示默认列表
            if(id === Player.defaultlist){
                loadList(id);
            }
            if(callback) {
                callback(id); // 调用回调函数
            }
            // 改变前端列表
            $(".sheet-item[data-no='" + id + "'] .sheet-cover").attr('src', musicList[id].cover);    // 专辑封面
            $(".sheet-item[data-no='" + id + "'] .sheet-name").html(musicList[id].name);     // 专辑名字

            // 调试信息输出
            if(Player.debug) {
                console.debug("歌单 [" + musicList[id].name+ "] 中的音乐获取成功");
            }
        }, //success

        /**
         success: function(jsonData){
            // 存储歌单信息
            var tempList = {
                id: lid,    // 列表的网易云 id
                name: jsonData.playlist.name,   // 列表名字
                cover: jsonData.playlist.coverImgUrl,   // 列表封面
                creatorName: jsonData.playlist.creator.nickname,   // 列表创建者名字
                creatorAvatar: jsonData.playlist.creator.avatarUrl,   // 列表创建者头像
                item: []
            };

            if(jsonData.playlist.coverImgUrl !== '') {
                tempList.cover = jsonData.playlist.coverImgUrl + "?param=200y200";
            } else {
                tempList.cover = musicList[id].cover;
            }

            if(typeof jsonData.playlist.tracks !== undefined || jsonData.playlist.tracks.length !== 0) {
                // 存储歌单中的音乐信息
                for (var i = 0; i < jsonData.playlist.tracks.length; i++) {
                    tempList.item[i] =  {
                        id: jsonData.playlist.tracks[i].id,  // 音乐ID
                        name: jsonData.playlist.tracks[i].name,  // 音乐名字
                        artist: jsonData.playlist.tracks[i].ar[0].name, // 艺术家名字
                        album: jsonData.playlist.tracks[i].al.name,    // 专辑名字
                        source: "netease",     // 音乐来源
                        url_id: jsonData.playlist.tracks[i].id,  // 链接ID
                        pic_id: null,  // 封面ID
                        lyric_id: jsonData.playlist.tracks[i].id,  // 歌词ID
                        pic: jsonData.playlist.tracks[i].al.picUrl + "?param=300y300",    // 专辑图片
                        url: null   // mp3链接
                    };
                }
            }

            // 歌单用户 id 不能丢
            if(musicList[id].creatorID) {
                tempList.creatorID = musicList[id].creatorID;
                if(musicList[id].creatorID === rem.uid) {   // 是当前登录用户的歌单，要保存到缓存中
                    var tmpUlist = playerReaddata('ulist');    // 读取本地记录的用户歌单
                    if(tmpUlist) {  // 读取到了
                        for(i=0; i<tmpUlist.length; i++) {  // 匹配歌单
                            if(tmpUlist[i].id == lid) {
                                tmpUlist[i] = tempList; // 保存歌单中的歌曲
                                playerSavedata('ulist', tmpUlist);  // 保存
                                break;
                            }
                        }
                    }
                }
            }

            // 存储列表信息
            musicList[id] = tempList;

            // 首页显示默认列表
            if(id === Player.defaultlist){
                loadList(id);
            }
            if(callback) {
                callback(id);    // 调用回调函数
            }

            // 改变前端列表
            $(".sheet-item[data-no='" + id + "'] .sheet-cover").attr('src', tempList.cover);    // 专辑封面
            $(".sheet-item[data-no='" + id + "'] .sheet-name").html(tempList.name);     // 专辑名字

            // 调试信息输出
            if(Player.debug) {
                console.debug("歌单 [" +tempList.name+ "] 中的音乐获取成功");
            }
        },   //success
         */
        error: function(XMLHttpRequest, textStatus, errorThrown) {
            layer.msg('歌单读取失败 - ' + XMLHttpRequest.status);
            console.error(XMLHttpRequest + textStatus + errorThrown);
            $(".sheet-item[data-no='" + id + "'] .sheet-name").html('<span style="color: #EA8383">读取失败</span>');     // 专辑名字
        } // error
    }); //ajax
}

// 完善获取音乐信息,获取url
function ajaxUrl(music, callback) {
    // 已经有数据,直接回调
    if(music.musicUrl !== null && music.musicUrl !== "err" && music.musicUrl !== "") {
        callback(music);
        return true;
    }
    // id为空，赋值链接错误。直接回调
    if(music.id === null) {
        music.musicUrl = "err";
        updateMusicInfo(music); // 更新音乐信息
        callback(music);
        return true;
    }
    
    $.ajax({ 
        type: Player.method,
        url: "/getMusicInfo",
        data: "id=" + music.id + "&source=" + music.source,
        dataType : "json",
        success: function(jsonData){
            // 调试信息输出
            if(Player.debug) {
                console.debug("歌曲链接：" + jsonData.musicUrl);
            }
            
            // 解决网易云音乐部分歌曲无法播放问题
            if(music.source === "1") {
                if(jsonData.musicUrl === "") {
                    jsonData.musicUrl = "https://music.163.com/song/media/outer/url?id=" + music.id + ".mp3";
                } else {
                    jsonData.musicUrl = jsonData.musicUrl.replace(/m7c.music./g, "m7.music.");
                    jsonData.musicUrl = jsonData.musicUrl.replace(/m8c.music./g, "m8.music.");
                }
            } else if(music.source === "baidu") {    // 解决百度音乐防盗链
                jsonData.musicUrl = jsonData.musicUrl.replace(/http:\/\/zhangmenshiting.qianqian.com/g, "https://gss0.bdstatic.com/y0s1hSulBw92lNKgpU_Z2jR7b2w6buu");
            }
            
            if(jsonData.musicUrl === "") {
                music.musicUrl = "err";
            } else {
                // 酷狗音乐加载音乐的时候,同时加载专辑图片
                if(jsonData.picUrl !== undefined && jsonData.picUrl !== null && jsonData.picUrl.indexOf("kugou") !== -1){
                    music.picUrl = jsonData.picUrl;
                }
                music.musicUrl = jsonData.musicUrl; // 记录结果
            }
            updateMusicInfo(music); // 更新音乐信息
            callback(music);    // 回调函数
            return true;
        },   //success
        error: function(XMLHttpRequest, textStatus, errorThrown) {
            layer.msg('歌曲链接获取失败 - ' + XMLHttpRequest.status);
            console.error(XMLHttpRequest + textStatus + errorThrown);
        }  // error
    }); //ajax
}

// 完善获取音乐封面图, 包含音乐信息的数组
function ajaxPic(music, callback) {
    // 已经有数据,直接回调
    if(music.picUrl !== null && music.picUrl !== "err" && music.picUrl !== "") {
        callback(music);
        return true;
    }
    // picId 为空，赋值链接错误。直接回调
    if(music.picId === null) {
        music.pic = "err";
        updateMusicInfo(music); // 更新音乐信息
        callback(music);
        return true;
    }
    
    // $.ajax({
    //     type: Player.method,
    //     url: Player.api,
    //     data: "types=pic&id=" + music.pic_id + "&source=" + music.source,
    //     dataType : "jsonp",
    //     success: function(jsonData){
    //         // 调试信息输出
    //         if(Player.debug) {
    //             console.log("歌曲封面：" + jsonData.url);
    //         }
    //
    //         if(jsonData.url !== "") {
    //             music.pic = jsonData.url;    // 记录结果
    //         } else {
    //             music.pic = "err";
    //         }
    //
    //         updateMinfo(music); // 更新音乐信息
    //
    //         callback(music);    // 回调函数
    //         return true;
    //     },   //success
    //     error: function(XMLHttpRequest, textStatus, errorThrown) {
    //         layer.msg('歌曲封面获取失败 - ' + XMLHttpRequest.status);
    //         console.error(XMLHttpRequest + textStatus + errorThrown);
    //     }   // error
    // }); //ajax
}

// ajax加载歌词, 参数：音乐ID
function ajaxLyric(music, callback) {
    lyricTip('歌词加载中...');
    
    if(!music.lyricId) callback('');  // 没有歌词ID，直接返回
    
    $.ajax({
        type: Player.method,
        url: "loadLyric",
        data: "id=" + music.lyricId + "&source=" + music.source,
        dataType : "json",
        success: function(jsonData){
            // 调试信息输出
            if (Player.debug) {
                console.debug("歌曲" + music.name + "歌词获取成功");
            }
            if (jsonData.lyric) {
                callback(jsonData.lyric, music.lyricId);    // 回调函数
            } else {
                callback('', music.lyricId);    // 回调函数
            }
        },   //success
        error: function(XMLHttpRequest, textStatus, errorThrown) {
            layer.msg('歌词读取失败 - ' + XMLHttpRequest.status);
            console.error(XMLHttpRequest + textStatus + errorThrown);
            callback('', music.lyricId);    // 回调函数
        }   // error   
    });//ajax
}

// ajax加载用户的播放列表
// 参数 用户的网易云 id
function ajaxUserList(uid) {
    var tmpLoading = layer.msg('加载中...', {icon: 16,shade: 0.01});
    $.ajax({
        type: Player.method,
        url: "",
        data: "types=userlist&uid=" + uid,
        dataType : "jsonp",
        complete: function(XMLHttpRequest, textStatus) {
            if(tmpLoading) layer.close(tmpLoading);    // 关闭加载中动画
        },  // complete
        success: function(jsonData){
            if(jsonData.code == "-1" || jsonData.code == 400){
                layer.msg('用户 uid 输入有误');
                return false;
            }
            
            if(jsonData.playlist.length === 0 || typeof(jsonData.playlist.length) === "undefined") {
                layer.msg('没找到用户 ' + uid + ' 的歌单');
                return false;
            }else{
                var tempList,userList = [];
                $("#sheet-bar").remove();   // 移除登陆条
                rem.uid = uid;  // 记录已同步用户 uid
                rem.uname = jsonData.playlist[0].creator.nickname;  // 第一个列表(喜欢列表)的创建者即用户昵称
                layer.msg('欢迎您 '+rem.uname);
                // 记录登录用户
                playerSaveData('uid', rem.uid);
                playerSaveData('uname', rem.uname);
                
                for (var i = 0; i < jsonData.playlist.length; i++) {
                    // 获取歌单信息
                    tempList = {
                        id: jsonData.playlist[i].id,    // 列表的网易云 id
                        name: jsonData.playlist[i].name,   // 列表名字
                        cover: jsonData.playlist[i].coverImgUrl  + "?param=200y200",   // 列表封面
                        creatorID: uid,   // 列表创建者id
                        creatorName: jsonData.playlist[i].creator.nickname,   // 列表创建者名字
                        creatorAvatar: jsonData.playlist[i].creator.avatarUrl,   // 列表创建者头像
                        item: []
                    };
                    // 存储并显示播放列表
                    addSheet(musicList.push(tempList) - 1, tempList.name, tempList.cover);
                    userList.push(tempList);
                }
                playerSaveData('ulist', userList);
                // 显示退出登录的提示条
                sheetBar();
            }
            // 调试信息输出
            if(Player.debug) {
                console.debug("用户歌单获取成功 [用户网易云ID：" + uid + "]");
            }
        },   //success
        error: function(XMLHttpRequest, textStatus, errorThrown) {
            layer.msg('歌单同步失败 - ' + XMLHttpRequest.status);
            console.error(XMLHttpRequest + textStatus + errorThrown);
        }   // error
    });//ajax
    return true;
}
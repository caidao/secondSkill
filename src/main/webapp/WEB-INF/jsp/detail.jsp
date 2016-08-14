
<%@page contentType="text/html; charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="zh-CN">
<head>

    <title>秒杀详情列表</title>
   <%@ include file="common/head.jsp"%>
</head>
<body>
    <!--页面显示部分-->
    <div class="container">
        <div class="panel panel-default text-center">
            <div class="pannel-heading ">
               <h2>${seckill.name}</h2>
            </div>
            <div class="pannel-body">
                <h2 class="text-danger">
                    <!--显示time图标-->
                    <span class="glyphicon glyphicon-time">
                    </span>
                    <!--展示倒计时-->
                    <span class="glyphicon" id="seckill-box"></span>
                </h2>
               <div id="killPhoneModal" class="modal fade">
                   <div class="modal-dialog">
                       <div class="modal-content">
                           <div class="modal-header">
                               <h3 class="modal-title tex-center">
                                   <span class="glyphicon glyphicon-phone"></span>秒杀电话:
                               </h3>
                           </div>
                           <div class="modal-body">
                               <div class="col-xs-8 col-xs-offset-2">
                                   <input type="text" name="killphone" id="killPhoneKey"
                                    placeholder="填手机好^o^" class="form-control">
                               </div>
                           </div>

                           <div class="modal-footer">
                               <span id="killPhoneMessage" class="glyphicon"></span>
                               <button type="button" id="killPhoneBtn" class="btn btn-success">
                                   <span class="glyphicon glyphicon-phone"></span>
                                   Submit
                               </button>
                           </div>
                       </div>
                   </div>
               </div>
            </div>
        </div>
    </div>

</body>


<!-- jQuery (necessary for Bootstrap's JavaScript plugins) -->
<script src="//cdn.bootcss.com/jquery/1.11.3/jquery.min.js"></script>
<!-- Include all compiled plugins (below), or include individual files as needed -->
<script src="//cdn.bootcss.com/bootstrap/3.3.5/js/bootstrap.min.js"></script>
<script src="//cdn.bootcss.com/jquery-cookie/1.4.1/jquery.cookie.js"></script>
<script src="//cdn.bootcss.com/jquery.countdown/2.1.0/jquery.countdown.js"></script>
<script src="/resource/js/seckill.js" type="text/javascript"></script>
<script type="text/javascript">
    $(function(){
        //是el表达式传入参数
        seckill.detail.init({
           seckillId:${seckill.seckillId},
            startTime:${seckill.startTime.time},
            endTime:${seckill.endTime.time}
        });
    });
</script>
</html>
package controllers;

import com.ning.http.client.providers.netty.Callback;
import com.ning.http.client.providers.netty.future.NettyResponseFuture;

import actors.PersonnelActor;
import dataAccess.MongoDBAccessor;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.WebSocket;
import views.html.*;

public class Application extends Controller {
	
    public Result index() {
        return ok(index.render("Your new application is ready."));
    }
    
    public WebSocket<String> socket() {
        return WebSocket.withActor(PersonnelActor::props);
    }
}

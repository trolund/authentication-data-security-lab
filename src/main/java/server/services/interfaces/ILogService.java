package server.services.interfaces;

import server.data.models.Log;

import java.util.List;

public interface ILogService {
    int addToServerLog(int userID, String action);
    List<Log> lastLogs(int take);
}

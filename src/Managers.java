public final class  Managers   {

    private Managers() {

    }

    static  TaskManager  getDefault() {
     return new InMemoryTaskManager();
  }

   static HistoryManager getDefaultHistory() {
       return new InMemoryHistoryManager();
    }
}

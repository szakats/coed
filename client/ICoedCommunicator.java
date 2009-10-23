package coed.client;

import coed.data.*;

public interface ICoedCommunicator {
   public final String STATUS_CONNECTED="Working online";
   public final String STATUS_ERROR="Could not connect to server";
   public final String STATUS_OFFLINE="Working offline";

   public String getState();

   public String getProjectList();
   public ICoedProject getProjectInfo(String name);
   public boolean checkoutProject(ICoedProject project);
   public boolean commitProject(ICoedProject project);

   public CoedFile getFileInfo();
   public boolean checkoutFile(CoedFile file);
   public boolean checkoutFiles(CoedFile[] files);
   public boolean commitFile(CoedFile file);
   public boolean commitFiles(CoedFile[] files);

   public boolean sendChanges(CoedFile file);
   public boolean getChanges(CoedFile file);

   public boolean requestLock(CoedFileLock lock);
   public boolean releaseLock(CoedFileLock lock);
}
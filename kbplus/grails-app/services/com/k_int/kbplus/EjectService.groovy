package com.k_int.kbplus

import com.k_int.kbplus.Org;
import groovy.util.logging.Slf4j
import java.util.UUID;

/**
 *
 */

@Slf4j
class EjectService {

  private boolean running = true;
  private Object exportRequestMonitor = new Object();
  private UUID instanceId = null;
  private static String PENDING_EXPORT_REQUESTS_QRY = '''
select o.id
from Org as o 
where o.exportStatus = :requested
and o.batchMonitorUUID is null
'''

  @javax.annotation.PostConstruct
  def init () {
    this.instanceId = UUID.randomUUID();
    log.info("EjectService::init - instance id is ${this.instanceId}");
    java.lang.Thread.startDaemon({
      this.watchExportRequests();
    })
  }

  private void watchExportRequests() {
    log.info("start watching export requests");
    try {
      while(running) {
        log.info("watchExportRequests()");
        synchronized(exportRequestMonitor) {
          exportRequestMonitor.wait(120000); //  Sleep 2m or until woken up
        }

        // Whilst there is work to do, continue generating exports
        while ( processNextExportRequest() ) {
          log.info("Processing an export request");
        }
      }
    }
    catch ( Exception e ) {
      log.error("Error in watchExportRequests thread",e);
    }
  }

  private boolean processNextExportRequest() {
    boolean work_done = false;
    List<Long> pending_requests = Org.executeQuery(PENDING_EXPORT_REQUESTS_QRY, [requested:'REQUESTED'])
    pending_requests.each { org_id ->
      boolean proceed=false;
      Org.withNewTransaction {
        Org o = Org.lock(org_id);
        if ( ( o.batchMonitorUUID == null ) && ( o.exportStatus == 'REQUESTED' ) ) {
          o.batchMonitorUUID = this.instanceId;
          o.save(flush:true, failOnError:true);
          proceed=true
        }
      }

      // If we have claimed the monitor for this instance
      if ( proceed ) {
      }
    }
    return work_done;
  }

  public void requestFreshExport(Org org) {
  }

}
package lead.exchange.controller;

import lead.exchange.service.ExternalEstateSyncService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/external")
public class ExternalSyncController {

    private final ExternalEstateSyncService syncService;

    @PostMapping("/sync-all")
    public void syncAll() {
        syncService.syncAll();
    }
}


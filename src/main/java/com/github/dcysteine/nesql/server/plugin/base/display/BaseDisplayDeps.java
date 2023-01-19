package com.github.dcysteine.nesql.server.plugin.base.display;

import com.github.dcysteine.nesql.sql.base.item.ItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/** Makes life easier for us by providing all required dependencies for display objects. */
@Service
public class BaseDisplayDeps {
    @Autowired
    private ItemRepository itemRepository;

    public ItemRepository getItemRepository() {
        return itemRepository;
    }
}

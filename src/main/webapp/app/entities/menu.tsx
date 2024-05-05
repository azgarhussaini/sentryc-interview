import React from 'react';
import { Translate } from 'react-jhipster';

import MenuItem from 'app/shared/layout/menus/menu-item';

const EntitiesMenu = () => {
  return (
    <>
      {/* prettier-ignore */}
      <MenuItem icon="asterisk" to="/marketplace">
        <Translate contentKey="global.menu.entities.marketplace" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/seller-info">
        <Translate contentKey="global.menu.entities.sellerInfo" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/seller">
        <Translate contentKey="global.menu.entities.seller" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/producer">
        <Translate contentKey="global.menu.entities.producer" />
      </MenuItem>
      {/* jhipster-needle-add-entity-to-menu - JHipster will add entities to the menu here */}
    </>
  );
};

export default EntitiesMenu;

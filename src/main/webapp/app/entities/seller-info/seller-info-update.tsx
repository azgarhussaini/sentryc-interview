import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IMarketplace } from 'app/shared/model/marketplace.model';
import { getEntities as getMarketplaces } from 'app/entities/marketplace/marketplace.reducer';
import { ISellerInfo } from 'app/shared/model/seller-info.model';
import { getEntity, updateEntity, createEntity, reset } from './seller-info.reducer';

export const SellerInfoUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const marketplaces = useAppSelector(state => state.marketplace.entities);
  const sellerInfoEntity = useAppSelector(state => state.sellerInfo.entity);
  const loading = useAppSelector(state => state.sellerInfo.loading);
  const updating = useAppSelector(state => state.sellerInfo.updating);
  const updateSuccess = useAppSelector(state => state.sellerInfo.updateSuccess);

  const handleClose = () => {
    navigate('/seller-info' + location.search);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getMarketplaces({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  // eslint-disable-next-line complexity
  const saveEntity = values => {
    const entity = {
      ...sellerInfoEntity,
      ...values,
      marketplaceId: marketplaces.find(it => it.id.toString() === values.marketplaceId?.toString()),
    };

    if (isNew) {
      dispatch(createEntity(entity));
    } else {
      dispatch(updateEntity(entity));
    }
  };

  const defaultValues = () =>
    isNew
      ? {}
      : {
          ...sellerInfoEntity,
          marketplaceId: sellerInfoEntity?.marketplaceId?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="sentryInterviewApp.sellerInfo.home.createOrEditLabel" data-cy="SellerInfoCreateUpdateHeading">
            <Translate contentKey="sentryInterviewApp.sellerInfo.home.createOrEditLabel">Create or edit a SellerInfo</Translate>
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              {!isNew ? (
                <ValidatedField
                  name="id"
                  required
                  readOnly
                  id="seller-info-id"
                  label={translate('sentryInterviewApp.sellerInfo.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('sentryInterviewApp.sellerInfo.marketplaceName')}
                id="seller-info-marketplaceName"
                name="marketplaceName"
                data-cy="marketplaceName"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                  maxLength: { value: 2048, message: translate('entity.validation.maxlength', { max: 2048 }) },
                }}
              />
              <ValidatedField
                label={translate('sentryInterviewApp.sellerInfo.url')}
                id="seller-info-url"
                name="url"
                data-cy="url"
                type="text"
                validate={{
                  maxLength: { value: 2048, message: translate('entity.validation.maxlength', { max: 2048 }) },
                }}
              />
              <ValidatedField
                label={translate('sentryInterviewApp.sellerInfo.country')}
                id="seller-info-country"
                name="country"
                data-cy="country"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('sentryInterviewApp.sellerInfo.externalId')}
                id="seller-info-externalId"
                name="externalId"
                data-cy="externalId"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                id="seller-info-marketplaceId"
                name="marketplaceId"
                data-cy="marketplaceId"
                label={translate('sentryInterviewApp.sellerInfo.marketplaceId')}
                type="select"
                required
              >
                <option value="" key="0" />
                {marketplaces
                  ? marketplaces.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <FormText>
                <Translate contentKey="entity.validation.required">This field is required.</Translate>
              </FormText>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/seller-info" replace color="info">
                <FontAwesomeIcon icon="arrow-left" />
                &nbsp;
                <span className="d-none d-md-inline">
                  <Translate contentKey="entity.action.back">Back</Translate>
                </span>
              </Button>
              &nbsp;
              <Button color="primary" id="save-entity" data-cy="entityCreateSaveButton" type="submit" disabled={updating}>
                <FontAwesomeIcon icon="save" />
                &nbsp;
                <Translate contentKey="entity.action.save">Save</Translate>
              </Button>
            </ValidatedForm>
          )}
        </Col>
      </Row>
    </div>
  );
};

export default SellerInfoUpdate;

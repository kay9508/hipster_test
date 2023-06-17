import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IBoardComment } from 'app/shared/model/board-comment.model';
import { getEntities as getBoardComments } from 'app/entities/board-comment/board-comment.reducer';
import { IBoardReComment } from 'app/shared/model/board-re-comment.model';
import { getEntity, updateEntity, createEntity, reset } from './board-re-comment.reducer';

export const BoardReCommentUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const boardComments = useAppSelector(state => state.boardComment.entities);
  const boardReCommentEntity = useAppSelector(state => state.boardReComment.entity);
  const loading = useAppSelector(state => state.boardReComment.loading);
  const updating = useAppSelector(state => state.boardReComment.updating);
  const updateSuccess = useAppSelector(state => state.boardReComment.updateSuccess);

  const handleClose = () => {
    navigate('/board-re-comment');
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getBoardComments({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    const entity = {
      ...boardReCommentEntity,
      ...values,
      boardComent: boardComments.find(it => it.id.toString() === values.boardComent.toString()),
      boardComment: boardComments.find(it => it.id.toString() === values.boardComment.toString()),
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
          ...boardReCommentEntity,
          boardComent: boardReCommentEntity?.boardComent?.id,
          boardComment: boardReCommentEntity?.boardComment?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="testApp.boardReComment.home.createOrEditLabel" data-cy="BoardReCommentCreateUpdateHeading">
            <Translate contentKey="testApp.boardReComment.home.createOrEditLabel">Create or edit a BoardReComment</Translate>
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
                  id="board-re-comment-id"
                  label={translate('testApp.boardReComment.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('testApp.boardReComment.content')}
                id="board-re-comment-content"
                name="content"
                data-cy="content"
                type="text"
                validate={{
                  maxLength: { value: 5000, message: translate('entity.validation.maxlength', { max: 5000 }) },
                }}
              />
              <ValidatedField
                label={translate('testApp.boardReComment.delAt')}
                id="board-re-comment-delAt"
                name="delAt"
                data-cy="delAt"
                check
                type="checkbox"
              />
              <ValidatedField
                id="board-re-comment-boardComent"
                name="boardComent"
                data-cy="boardComent"
                label={translate('testApp.boardReComment.boardComent')}
                type="select"
              >
                <option value="" key="0" />
                {boardComments
                  ? boardComments.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField
                id="board-re-comment-boardComment"
                name="boardComment"
                data-cy="boardComment"
                label={translate('testApp.boardReComment.boardComment')}
                type="select"
              >
                <option value="" key="0" />
                {boardComments
                  ? boardComments.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/board-re-comment" replace color="info">
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

export default BoardReCommentUpdate;

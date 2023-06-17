import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IBoard } from 'app/shared/model/board.model';
import { getEntities as getBoards } from 'app/entities/board/board.reducer';
import { IBoardComment } from 'app/shared/model/board-comment.model';
import { getEntity, updateEntity, createEntity, reset } from './board-comment.reducer';

export const BoardCommentUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const boards = useAppSelector(state => state.board.entities);
  const boardCommentEntity = useAppSelector(state => state.boardComment.entity);
  const loading = useAppSelector(state => state.boardComment.loading);
  const updating = useAppSelector(state => state.boardComment.updating);
  const updateSuccess = useAppSelector(state => state.boardComment.updateSuccess);

  const handleClose = () => {
    navigate('/board-comment');
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getBoards({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    const entity = {
      ...boardCommentEntity,
      ...values,
      board: boards.find(it => it.id.toString() === values.board.toString()),
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
          ...boardCommentEntity,
          board: boardCommentEntity?.board?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="testApp.boardComment.home.createOrEditLabel" data-cy="BoardCommentCreateUpdateHeading">
            <Translate contentKey="testApp.boardComment.home.createOrEditLabel">Create or edit a BoardComment</Translate>
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
                  id="board-comment-id"
                  label={translate('testApp.boardComment.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('testApp.boardComment.content')}
                id="board-comment-content"
                name="content"
                data-cy="content"
                type="text"
                validate={{
                  maxLength: { value: 5000, message: translate('entity.validation.maxlength', { max: 5000 }) },
                }}
              />
              <ValidatedField
                label={translate('testApp.boardComment.delAt')}
                id="board-comment-delAt"
                name="delAt"
                data-cy="delAt"
                check
                type="checkbox"
              />
              <ValidatedField
                id="board-comment-board"
                name="board"
                data-cy="board"
                label={translate('testApp.boardComment.board')}
                type="select"
              >
                <option value="" key="0" />
                {boards
                  ? boards.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField
                id="board-comment-board"
                name="board"
                data-cy="board"
                label={translate('testApp.boardComment.board')}
                type="select"
              >
                <option value="" key="0" />
                {boards
                  ? boards.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/board-comment" replace color="info">
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

export default BoardCommentUpdate;

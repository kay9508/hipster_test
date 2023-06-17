import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import BoardComment from './board-comment';
import BoardCommentDetail from './board-comment-detail';
import BoardCommentUpdate from './board-comment-update';
import BoardCommentDeleteDialog from './board-comment-delete-dialog';

const BoardCommentRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<BoardComment />} />
    <Route path="new" element={<BoardCommentUpdate />} />
    <Route path=":id">
      <Route index element={<BoardCommentDetail />} />
      <Route path="edit" element={<BoardCommentUpdate />} />
      <Route path="delete" element={<BoardCommentDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default BoardCommentRoutes;

<%@ page import="org.evgenysav.infrastructure.dto.classes_dto.VehicleTypeDto" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Comparator" %>
<%@ page import="java.util.ArrayList" %>
<%@ page contentType="text/html;charset=utf-8" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Типы автомобилей</title>
    <link href="${pageContext.request.contextPath}/resources/css/style.css" rel="stylesheet">
</head>
<body>
<div class="center flex full-vh">
    <div class="vertical-center">
        <a class="ml-20" href="${pageContext.request.contextPath}/">На главную</a>
        <br/>
        <br/>
        <hr/>
        <br/>
        <%
            String sortKey = null;
            String order = null;
            if (request.getParameter("name") != null) sortKey = "name";
            if (request.getParameter("tax") != null) sortKey = "tax";
            if (request.getParameter("asc") != null) order = "asc";
            if (request.getParameter("desc") != null) order = "desc";
        %>
        <%if (sortKey != null) {%>
        <%
            String clearPath = "/viewTypes";
            String ascPath = "?" + sortKey + "&asc";
            String descPath = "?" + sortKey + "&desc";
        %>
        <div>
            <a class="ml-20" href="<%=descPath%>">Сортировать по убыванию</a>
            <a class="ml-20" href="<%=ascPath%>">Сортировать по возрастанию</a>
            <a class="ml-20" href="<%=clearPath%>">Очистить параметр</a>
        </div>
        <br/>
        <hr/>
        <br/>
        <%}%>
        <table>
            <caption>Типы авто</caption>
            <tr>
                <th>Название</th>
                <th>Коэффициент налога</th>
            </tr>
            <%
                List<VehicleTypeDto> dtoList = (List<VehicleTypeDto>) request.getAttribute("types");
                List<VehicleTypeDto> listToSort = new ArrayList<>(dtoList);
                Comparator<VehicleTypeDto> comparator = null;
                if (sortKey != null && sortKey.equals("name")) {
                    comparator = Comparator.comparing(VehicleTypeDto::getName);
                }
                if (sortKey != null && sortKey.equals("tax")) {
                    comparator = Comparator.comparingDouble(VehicleTypeDto::getTaxCoefficient);
                }
                if (order != null && comparator != null && order.equals("desc")) {
                    comparator = comparator.reversed();
                }
                if (comparator != null) {
                    listToSort.sort(comparator);
                }
                for (VehicleTypeDto dto : listToSort) {
            %>
            <tr>
                <td><%=dto.getName()%>
                </td>
                <td><%=dto.getTaxCoefficient()%>
                </td>
            </tr>
            <%}%>
        </table>
        <%
            if (dtoList.size() > 0) {
        %>
        <p>Минимальный коэффициент налога:
            <strong><%=dtoList.stream()
                    .map(VehicleTypeDto::getTaxCoefficient)
                    .min(Double::compare)
                    .get()%>
            </strong></p>
        <p>Максимальный коэффициент налога:
            <strong><%=dtoList.stream()
                    .map(VehicleTypeDto::getTaxCoefficient)
                    .max(Double::compare)
                    .get()%>
            </strong></p>
        <%}%>
        <br/>
        <hr/>
        <br/>
        <div>
            <% if (request.getParameter("name") == null) {%><a class="ml-20"
                                                               href="${pageContext.request.contextPath}/viewTypes?name">Сортировать
            по типу</a><%}%>
            <% if (request.getParameter("tax") == null) {%><a class="ml-20"
                                                              href="${pageContext.request.contextPath}/viewTypes?tax">Сортировать
            по налогу</a><%}%>
        </div>
    </div>
</div>
</body>
</html>
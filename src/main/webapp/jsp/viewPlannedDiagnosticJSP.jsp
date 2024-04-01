<%@ page import="org.evgenysav.infrastructure.dto.classes_dto.VehicleDto" %>
<%@ page import="java.util.List" %>
<%@ page import="org.evgenysav.infrastructure.core.impl.ApplicationContext" %>
<%@ page import="org.evgenysav.classes_.Workroom" %>
<%@ page contentType="text/html;charset=utf-8" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Автомобили</title>
    <link href="${pageContext.request.contextPath}/resources/css/style.css" rel="stylesheet">
</head>
<body>
<div class="center flex full-vh">
    <div class="vertical-center">
        <%
            List<VehicleDto> dtoList = (List<VehicleDto>) request.getAttribute("cars");
            ApplicationContext applicationContext = (ApplicationContext) request.getServletContext().getAttribute("applicationContext");
            Workroom workroom = applicationContext.getObject(Workroom.class);
            dtoList.forEach(workroom::detectBreaking);
        %>

        <a class="ml-20" href="/">На главную</a>
        <br/>
        <br/>
        <hr/>
        <p>Период 15 минут</p>
        <p>Последняя была 10 минут назад</p>
        <br/>
        <table>
            <caption>Автомобили после диагностики</caption>
            <tr>
                <th>Тип</th>
                <th>Модель</th>
                <th>Регистрационный номер</th>
                <th>Масса</th>
                <th>Дата выпуска</th>
                <th>Цвет</th>
                <th>Тип двигателя</th>
                <th>Пробег</th>
                <th>Бак</th>
                <th>Была исправна</th>
                <th>Починена</th>
            </tr>

            <%if (dtoList.size() == 0) {%>
            <tr>
                <td colspan="10">
                    Авто с заданными параметрами не найдены
                </td>
            </tr>
            <%}%>
            <%
                for (VehicleDto dto : dtoList) {
            %>
            <tr>
                <td><%=dto.getTypeName()%>
                </td>
                <td><%=dto.getModelName()%>
                </td>

                <td><%=dto.getRegistrationNumber()%>
                </td>

                <td><%=dto.getWeight()%>
                </td>

                <td><%=dto.getManufactureYear()%>
                </td>

                <td><%=dto.getColor()%>
                </td>
                <td><%=dto.getEngineName()%>
                </td>

                <td><%=dto.getMileage()%>
                </td>

                <td><%= dto.getTankVolume()%>
                </td>
                <%
                    String wasNormal = "";
                    String wasRepaired = "";
                    if (workroom.wasBroken(dto)) {
                        wasRepaired = "да";
                        wasNormal = "нет";
                    } else {
                        wasRepaired = "нет";
                        wasNormal = "да";
                    }
                %>
                <td>
                    <%=wasNormal%>
                </td>
                <td><%=
                wasRepaired
                %>
                </td>
            </tr>
            <%}%>
            <% dtoList.forEach(workroom::repair);%>
        </table>
        <br/>
    </div>
</div>
</body>
</html>